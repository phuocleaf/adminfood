from pathlib import Path

from fastapi import APIRouter, UploadFile
import os
import mysql.connector
from starlette.responses import FileResponse
from datetime import datetime
from pydantic import BaseModel
from model.cart import CartItem
from typing import Union
admin = APIRouter()

# Connect to database
mydb = mysql.connector.connect(
    host="localhost",
    user="root",
    password="",
    database="food"
)


# @admin.post("/admin/check")
# async def login(admin_email: str, admin_password: str):
#     cursor = mydb.cursor()
#     cursor.execute("SELECT * FROM admin WHERE admin_email = %s AND admin_password = %s", (admin_email, admin_password,))
#     result = cursor.fetchone()
#
#     if result is None:
#         return 0
#     else:
#         cursor.execute("SELECT admin_id FROM admin WHERE admin_email = %s AND admin_password = %s",
#                        (admin_email, admin_password,))
#         result = cursor.fetchone()
#         return result[0]
#
#
# @admin.get("/order/{status}")
# async def get_order_by_id(status: int):
#     cursor = mydb.cursor()
#     cursor.execute("SELECT * FROM orders WHERE order_status = %s", (status,))
#     result = cursor.fetchall()
#     orders = []
#     for item in result:
#         order = {
#             "user_id": item[0],
#             "order_id": item[1],
#             "order_datetime": item[2],
#             "order_address": item[3],
#             "order_total": item[4],
#             "order_status": item[5]
#         }
#         orders.append(order)
#
#     return {"orders": orders}
#
#
# #SELECT user_name, user_phone, order_id, order_datetime, order_address, order_total, order_status FROM orders JOIN user ON orders.user_id = user.user_id WHERE order_status = 1;
#
# @admin.get("/full_order/{order_status}")
# async def get_order_detail_by_id(order_status: int):
#     cursor = mydb.cursor()
#     status = int(order_status)
#     cursor.execute("SELECT user_name, user_phone, "
#                    "order_id, order_datetime, order_address, order_total, order_status "
#                    "FROM orders JOIN user ON orders.user_id = user.user_id "
#                    "WHERE order_status = %s ORDER BY order_id DESC", (status,))
#     result = cursor.fetchall()
#     orders = []
#     for item in result:
#         order = {
#             "user_name": item[0],
#             "user_phone": item[1],
#             "order_id": item[2],
#             "order_datetime": item[3],
#             "order_address": item[4],
#             "order_total": item[5],
#             "order_status": item[6]
#         }
#         orders.append(order)
#
#     return {"orders": orders}
#
#
# @admin.put("/change_status/{order_id}/{status}")
# async def change_status(order_id: int, status: int):
#     cursor = mydb.cursor()
#     cursor.execute("UPDATE orders SET order_status = %s WHERE order_id = %s", (status, order_id,))
#     mydb.commit()
#     return 1
#
#
# @admin.delete("/delete/{order_id}")
# async def delete_order(order_id: int):
#     cursor = mydb.cursor()
#     cursor.execute("DELETE FROM orders WHERE order_id = %s", (order_id,))
#     cursor.execute("DELETE FROM detail WHERE order_id = %s", (order_id,))
#     mydb.commit()
#     return 1

