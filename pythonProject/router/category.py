from pathlib import Path

from fastapi import APIRouter, UploadFile
import os
import mysql.connector
from starlette.responses import FileResponse
from pydantic import BaseModel

# http://127.0.0.1:8000
cate = APIRouter()

# Connect to database
mydb = mysql.connector.connect(
    host="localhost",
    user="root",
    password="",
    database="foodapp"
)


@cate.get("/")
def get():
    return {"message": "Hello World"}


@cate.get("/category")
def get_category():
    cursor = mydb.cursor()
    cursor.execute("SELECT * FROM category")
    result = cursor.fetchall()
    categories = []
    for item in result:
        category = {
            "id": item[0],
            "category": item[1],
            "categoryThumb": item[2],
            "categoryDescription": item[3],
        }
        categories.append(category)

    return {"categories": categories}


@cate.get("/category/{id}")
def get_category_by_id(id: int):
    cursor = mydb.cursor()
    cursor.execute("SELECT * FROM category WHERE id = %s", (id,))
    result = cursor.fetchone()
    category = {
        "id": result[0],
        "category": result[1],
        "categoryThumb": result[2],
        "categoryDescription": result[3],
    }
    return category


@cate.post("/category/create")
def add_category(category: str, categoryThumb: UploadFile, categoryDescription: str):
    file_path = os.path.join("images/", categoryThumb.filename)
    with open(file_path, "wb") as image_file:
        image_file.write(categoryThumb.file.read())
    cursor = mydb.cursor()
    sql_path = "http://10.3.75.182:8000/get_image/" + categoryThumb.filename
    # http://127.0.0.1:8000/get_image/MT10.jpg
    # http://10.3.75.30:8000/get_image/MT10.jpg
    cursor.execute("INSERT INTO category (category, categoryThumb, categoryDescription) VALUES (%s, %s, %s)",
                   (category, sql_path, categoryDescription))
    mydb.commit()
    return {"message": "Category added successfully"}


@cate.put("/category/update/{id}")
def update_category(id: int, category: str, categoryThumb: str, categoryDescription: str):
    cursor = mydb.cursor()
    cursor.execute("UPDATE category SET category = %s, categoryThumb = %s, categoryDescription = %s WHERE id = %s",
                   (category, categoryThumb, categoryDescription, id))
    mydb.commit()
    return {"message": "Category updated successfully"}


@cate.get("/get_image/{image_name}")
async def get_image(image_name: str):
    image_path = f"images/{image_name}"
    return FileResponse(image_path)


@cate.delete("/category/delete/{id}")
def delete_category(id: int):
    cursor = mydb.cursor()
    cursor.execute("DELETE FROM category WHERE id = %s", (id,))
    mydb.commit()
    return {"message": "Category deleted successfully"}


class GreetingModel(BaseModel):
    id: int
    text: str
    desc: str


@cate.post("/greeting")
def greeting(greeting: GreetingModel):
    return {"message": greeting}


class CategoryModel(BaseModel):
    category: str
    categoryThumb: str
    categoryDescription: str


@cate.post("/category/taomoi")
def create_category(category: CategoryModel):
    cursor = mydb.cursor()
    cursor.execute("INSERT INTO category (category, categoryThumb, categoryDescription) VALUES (%s, %s, %s)",
                   (category.category, category.categoryThumb, category.categoryDescription))
    mydb.commit()
    return {"message": "Category added successfully"}


