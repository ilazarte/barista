class Animal
  constructor: (@name) ->
  
  @clzstatic: "fool";

  field: "notfool"; 

  move: (meters) ->
    console.log @name + " moved #{meters}m. for #{@field}"

sam = new Animal("cat")
sam.move()

tom = new Animal("rhino")
tom.field = "isfool"
tom.move()

sam.move()