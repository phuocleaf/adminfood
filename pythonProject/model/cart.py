

from pydantic import BaseModel

class CartItem(BaseModel):
    food_id: int
    food_image: str
    food_name: str
    food_price: int
    quantity: int


