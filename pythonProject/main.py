# FoodAppApi => pythonProject

from fastapi import FastAPI

from router.food import food

from router.admin import admin

from router.category import cate

app = FastAPI()

app.include_router(food)
#app.include_router(admin)

app.include_router(cate)




