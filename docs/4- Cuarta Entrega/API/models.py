from pydantic import BaseModel
from sqlalchemy import Column, Integer, String
from database import Base

class LugarDB(Base):
    __tablename__ = 'lugar'
    id = Column(Integer, primary_key=True, index=True)
    nombre = Column(String(255))
    direccion = Column(String(255))
    latitud = Column(String(255))
    longitud = Column(String(255))