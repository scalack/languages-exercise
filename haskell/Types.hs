module Types(
Shape(..)
,Vec(..)

) where

data Bool = False | True
data Shape = Circle Float Float Float |Rectangle Float Float Float Float deriving (Show)
surface (Circle _ _ r) = pi * r^2
surface (Rectangle x1 y1 x2 y2) = (abs $ x2 - x1) * (abs $ y2 -y1)

{- record syntax -}

data Person = Person String String Int deriving (Show)
data Car = Car { a::String,b::String,c::Int} deriving (Show)
data Vec a = Vec  a a a| Vec2 a a  deriving (Show)



