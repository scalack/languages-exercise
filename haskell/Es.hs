module List where

data List a
    = Null
    | Node (List a) a
    deriving (Show, Eq)

build = build' Null
    where
    build' Null  []    = Null
    build' list (x:xs) = Node (build' list xs) x

head Null          = Nothing
head (Node list x) = Just x

tail Null          = Nothing
tail (Node list x) = Just list

toe Null           = Nothing
toe (Node Null  x) = Just x
toe (Node list x)  = toe list

display Null = "Null"
display (Node Null x) = show x
display (Node list x) = show x ++ " => " ++ display list

toList Null          = []
toList (Node list x) = x : toList list

append Null          x = Node Null x
append (Node list y) x = Node (append list x) y

reverse Null          = Null
reverse (Node list a) = (List.reverse list) `append` a

join sep Null          = ""
join sep (Node Null x) = x
join sep (Node list x) = x ++ sep ++ join sep list

map f Null          = Null
map f (Node list x) = Node (List.map f list) (f x)

step = step' ""
    where
    step' sep Null          = ""
    step' []  (Node list x) = []  ++ "> " ++ x ++ "\n" ++ step' ([]  ++ "-") list
    step' sep (Node list x) = sep ++ "> " ++ x ++ "\n" ++ step' (sep ++ "-") list

main =
    do
    let xs = build [1 .. 9]
    let sx = List.reverse xs
    let aa = List.map show sx
    -- let bb = join " --> " aa
    let bb = step aa
    putStrLn bb
