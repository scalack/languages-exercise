-----------------------------------------------------------------------------
-- | Logo.hs
-- Module : Logo
-- Copyright : (c) 2011 Boyun Tang
-- License : BSD-style
-- Maintainer : tangboyun@hotmail.com
-- Stability : experimental
-- Portability : ghc
-- 简单的Logo绘图工具
-- 
--
-----------------------------------------------------------------------------
module Logo
       (
         Logo
        ,Turtle
        ,initTurtle
        ,move
        ,forward
        ,turn
        ,resize
        ,recDraw
        ,runLogo
       )
       where
import Control.Monad
import Control.Arrow
import Control.Monad.Writer
import Graphics.Rendering.Cairo

type Vec = (Double,Double)
type Point = (Double,Double)

newtype Turtle = T {
  runT :: (Point,Vec)
  }

type Logo = Writer [Render ()] Turtle

initTurtle :: Double -> Double -> Logo
initTurtle x y = writer (T ( (x,y),(1,0.0) ),[moveTo x y])

-- | 角度与弧度的转换  
angToPi :: Double -> Double
angToPi a = 2.0 * pi * a / 360.0 

-- | 移动但不画线
move :: Double -> Turtle -> Logo
move d (T ( (x,y),w@(u,v) ) ) =
  let x' = x+d*u
      y' = y+d*v
  in writer (T ( (x',y'), w),[moveTo x' y'])

-- | 转向，逆时针为正
turn :: Double -> Turtle -> Logo
turn a (T (p,(u,v) ) )  =
    let a' = angToPi (-a)
   
    in   writer (T (p,(u*cos a' - v*sin a', u*sin a' + v*cos a') ),[])



-- | 线性改变之后的路径大小

resize :: Double -> Turtle -> Logo
resize s (T (p,w) ) = writer (T (p, join (***) (s *) w),[])

-- | 移动且画线
forward :: Double -> Turtle -> Logo
forward d (T ( (x,y),w@(u,v) ) ) =
  let x' = x+d*u
      y' = y+d*v
  in writer (T ( (x',y'), w),[lineTo x' y'])

-- | 简单的递归绘制接口
recDraw :: (Turtle -> Logo) -- ^ 递归调用绘制的基本图形
        -> (Turtle -> Logo) -- ^ 每次调用之后的后处理操作
        -> Int        -- ^ 递归深度
        -> Turtle          -- ^ 起始点
        -> Logo
recDraw drawFunc afterDraw n t = go n t
  where
    go n | n > 0 = \input -> drawFunc input >>= afterDraw >>= go (n - 1)
         | otherwise = return 

-- | 抽取Monad，顺序执行cairo绘制操作。
runLogo :: Logo -> Render ()
runLogo = sequence_ . execWriter
-----------------------------------------------------------------------------
-- | Toy.hs
-- Module : Main
-- Copyright : (c) 2011 Boyun Tang
-- License : BSD-style
-- Maintainer : tangboyun@hotmail.com
-- Stability : experimental
-- Portability : ghc
--
-- 
--
-----------------------------------------------------------------------------
module Main where
import Logo
import Graphics.Rendering.Cairo
import Control.Monad

main :: IO ()
main = do
  sur <- createImageSurface FormatARGB32 600 400 -- 600 × 400 像素
  renderWith sur myPic
  surfaceWriteToPNG sur "Star.png" 

-- | 参数分别为转角、边长、起始点
drawStar :: Double -> Double -> Turtle -> Logo
drawStar a d i = go 5 i
  where
    go time | time > 0 = \i -> forward d i >>= turn a >>= go (time - 1)
            | otherwise = return

-- | 螺线，递归地画星星，每画完一个以后转a度，并且收缩s倍，递归n次
spiral :: Double -> Double -> Double -> Int -> Turtle -> Logo
spiral s a d n =
  turn (-a) >=>
  recDraw (drawStar (-144.0) d)
          (turn  a >=> resize s) n

myPic = do
  setSourceRGB 0 0 0  -- 黑色
  setLineWidth 1.2    -- 加粗1.2倍
  runLogo $ initTurtle 200 180 >>= spiral 0.9 (-18) 300 40 -- 起始坐标（200,180）
  stroke