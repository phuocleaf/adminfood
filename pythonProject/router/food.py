from pathlib import Path

from fastapi import APIRouter, UploadFile
import os
import mysql.connector
from starlette.responses import FileResponse
from datetime import datetime
from pydantic import BaseModel
from model.cart import CartItem

food = APIRouter()

# Connect to database
mydb = mysql.connector.connect(
    host="localhost",
    user="root",
    password="",
    database="food"
)


@food.post("/type/create")
async def add_type(type_name: str):
    cursor = mydb.cursor()
    cursor.execute("INSERT INTO type (type_name) VALUES (%s)", (type_name,))
    mydb.commit()
    return {"message": "Type added successfully"}


@food.post("/food/create")
async def add_food(food_name: str, food_desc: str, food_price: int, food_image: str, type_id: int):
    cursor = mydb.cursor()
    cursor.execute("INSERT INTO food (food_name, food_desc, food_price, food_image, type_id) "
                   "VALUES (%s, %s, %s, %s, %s)", (food_name, food_desc, food_price, food_image, type_id,))
    mydb.commit()
    return {"message": "Food added successfully"}


@food.get("/food")
async def get_food():
    cursor = mydb.cursor()
    cursor.execute("SELECT * FROM food")
    result = cursor.fetchall()
    foods = []
    for item in result:
        food = {
            "food_id": item[0],
            "food_name": item[1],
            "food_desc": item[2],
            "food_image": item[3],
            "food_price": item[4],
            "type_id": item[5]
        }
        foods.append(food)

    return {"foods": foods}


@food.get("/food/{id}")
async def get_food_by_id(id: int):
    cursor = mydb.cursor()
    cursor.execute("SELECT * FROM food WHERE food_id = %s", (id,))
    result = cursor.fetchone()
    food = {
        "food_id": result[0],
        "food_name": result[1],
        "food_desc": result[2],
        "food_image": result[3],
        "food_price": result[4],
        "type_id": result[5]
    }
    return food


@food.get("/food/type/{id}")
async def get_food_by_type_id(type_id: int):
    cursor = mydb.cursor()
    cursor.execute("SELECT * FROM food WHERE type_id = %s", (type_id,))
    result = cursor.fetchall()
    foods = []
    for item in result:
        food = {
            "food_id": item[0],
            "food_name": item[1],
            "food_desc": item[2],
            "food_image": item[3],
            "food_price": item[4],
            "type_id": item[5]
        }
        foods.append(food)

    return {"foods": foods}


@food.post("/user/signup")
async def add_user(user_name: str, user_email: str, user_password: str, user_address: str, user_phone: str):
    cursor = mydb.cursor()
    cursor.execute("SELECT * FROM user WHERE user_email = %s", (user_email,))
    result = cursor.fetchone()
    if result is None:
        cursor.execute("INSERT INTO user (user_name, user_email, user_password, user_address, user_phone) "
                       "VALUES (%s, %s, %s, %s, %s)", (user_name, user_email, user_password, user_address, user_phone,))
        mydb.commit()
        return 1
    else:
        return 0


@food.post("/user/login")
async def login(user_email: str, user_password: str):
    cursor = mydb.cursor()
    cursor.execute("SELECT * FROM user WHERE user_email = %s AND user_password = %s", (user_email, user_password,))
    result = cursor.fetchone()

    if result is None:
        return 0
    else:
        cursor.execute("SELECT user_id FROM user WHERE user_email = %s AND user_password = %s",
                       (user_email, user_password,))
        result = cursor.fetchone()
        return result[0]


@food.get("/name/{id}")
async def get_name(id: int):
    cursor = mydb.cursor()
    cursor.execute("SELECT user_name FROM user WHERE user_id = %s", (id,))
    result = cursor.fetchone()
    return result[0]


@food.post("/order/create")
async def create_order(user_id: int, order_total: int, cart_list: list[CartItem]):
    cursor = mydb.cursor()
    cursor.execute("SELECT user_address FROM user WHERE user_id = %s", (user_id,))
    result_address = cursor.fetchone()
    cursor.execute("INSERT INTO orders (user_id, order_total, order_address,order_status,order_datetime) "
                   "VALUES (%s, %s, %s,%s,CURRENT_TIMESTAMP())",
                   (user_id, order_total, result_address[0], 1,))
    mydb.commit()
    cursor.execute("SELECT order_id FROM orders WHERE user_id = %s ORDER BY order_id DESC LIMIT 1", (user_id,))
    result = cursor.fetchone()
    order_id = result[0]
    for item in cart_list:
        cursor.execute("INSERT INTO detail (order_id, food_id, food_name, food_image, food_price, detail_quantity) "
                       "VALUES (%s, %s, %s, %s, %s, %s)",
                       (order_id, int(item.food_id), item.food_name, item.food_image, item.food_price,
                        int(item.quantity),))
        mydb.commit()

    return 1


@food.get("/user/{id}")
async def get_user_by_id(id: int):
    cursor = mydb.cursor()
    cursor.execute("SELECT user_name, user_email, user_address, user_phone FROM user WHERE user_id = %s", (id,))
    result = cursor.fetchone()
    user = {
        "user_name": result[0],
        "user_email": result[1],
        "user_address": result[2],
        "user_phone": result[3]
    }
    return user


@food.get("/order/{id}/{status}")
async def get_order_by_id(id: int, status: int):
    cursor = mydb.cursor()
    cursor.execute("SELECT * FROM orders WHERE user_id = %s AND order_status = %s ORDER BY order_id DESC", (id, status,))
    result = cursor.fetchall()
    orders = []
    for item in result:
        order = {
            "user_id": item[0],
            "order_id": item[1],
            "order_datetime": item[2],
            "order_address": item[3],
            "order_total": item[4],
            "order_status": item[5]
        }
        orders.append(order)

    return {"orders": orders}


@food.get("/detail/{order_id}")
async def get_detail_by_order_id(order_id: int):
    cursor = mydb.cursor()
    cursor.execute("SELECT * FROM detail WHERE order_id = %s", (order_id,))
    result = cursor.fetchall()
    details = []
    for item in result:
        detail = {
            "detail_id": item[0],
            "order_id": item[1],
            "food_id": item[2],
            "food_name": item[3],
            "food_image": item[4],
            "food_price": item[5],
            "detail_quantity": item[6]
        }
        details.append(detail)

    return {"details": details}

#admin


@food.post("/admin/check")
async def login(admin_email: str, admin_password: str):
    cursor = mydb.cursor()
    cursor.execute("SELECT * FROM admin WHERE admin_email = %s AND admin_password = %s", (admin_email, admin_password,))
    result = cursor.fetchone()

    if result is None:
        return 0
    else:
        cursor.execute("SELECT admin_id FROM admin WHERE admin_email = %s AND admin_password = %s",
                       (admin_email, admin_password,))
        result = cursor.fetchone()
        return result[0]


@food.get("/order/{status}")
async def get_order_by_id(status: int):
    cursor = mydb.cursor()
    cursor.execute("SELECT * FROM orders WHERE order_status = %s ORDER BY order_id DESC", (status,))
    result = cursor.fetchall()
    orders = []
    for item in result:
        order = {
            "user_id": item[0],
            "order_id": item[1],
            "order_datetime": item[2],
            "order_address": item[3],
            "order_total": item[4],
            "order_status": item[5]
        }
        orders.append(order)

    return {"orders": orders}


#SELECT user_name, user_phone, order_id, order_datetime, order_address, order_total, order_status FROM orders JOIN user ON orders.user_id = user.user_id WHERE order_status = 1;

@food.get("/full_order/{order_status}")
async def get_order_detail_by_id(order_status: int):
    cursor = mydb.cursor()
    status = int(order_status)
    cursor.execute("SELECT user_name, user_phone, "
                   "order_id, order_datetime, order_address, order_total, order_status "
                   "FROM orders JOIN user ON orders.user_id = user.user_id "
                   "WHERE order_status = %s ORDER BY order_id DESC", (status,))
    result = cursor.fetchall()
    orders = []
    for item in result:
        order = {
            "user_name": item[0],
            "user_phone": item[1],
            "order_id": item[2],
            "order_datetime": item[3],
            "order_address": item[4],
            "order_total": item[5],
            "order_status": item[6]
        }
        orders.append(order)

    return {"orders": orders}


@food.put("/change_status/{order_id}/{status}")
async def change_status(order_id: int, status: int):
    cursor = mydb.cursor()
    cursor.execute("UPDATE orders SET order_status = %s WHERE order_id = %s", (status, order_id,))
    mydb.commit()
    return 1


@food.delete("/delete/{order_id}")
async def delete_order(order_id: int):
    cursor = mydb.cursor()
    cursor.execute("DELETE FROM orders WHERE order_id = %s", (order_id,))
    cursor.execute("DELETE FROM detail WHERE order_id = %s", (order_id,))
    mydb.commit()
    return 1



