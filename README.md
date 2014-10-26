watermelon2014
==============

 Project 3: Watermelon  
 
The genetics of watermelon breeding are quite interesting. One class of natural watermelons is diploid, meaning that (like humans) there are two copies of each chromosome, one inherited from each parent. A second class is tetraploid: two copies of each chromosome are inherited from each parent, making four copies in total.  An intriguing result of breeding a diploid plant with a tetraploid plant is that the progeny is triploid. A side-effect of triploidy is that the plant produces almost no seeds. Interbreeding would be selected against in the wild, since seedless watermelons would not reproduce. Nevertheless, humans can overcome this barrier by explicitly creating conditions in which diploid and tetraploid plants pollinate each other. In fact, humans are motivated to do so because seedless watermelons are just so much easier to eat!  Your job is to plant a field with diploid or tetraploid seeds in such a way as to maximize the output of fruit from a field. The metric rewards the production of seedless watermelons: The reward for producing a seedless watermelon is one dollar, whereas the reward for producing a watermelon containing seeds is s dollars, where 0&lt;s&lt;1. s is a parameter that we will vary.  You will be given a rectangular field of dimensions L by W meters. This field has a number of pre-existing trees within it, whose coordinates are specified. A seed must be planted in such a way that it has exclusive access to soil within one meter. That means each seed must be at least one meter from any field boundary, and at least two meters from all other seeds and trees. You specify the location of each seed, and its ploidy.  Bees pollinate the crop by flying between plants. Since bees are more likely to fly between nearby plants, the probability of pollination is higher when plants are close together.

Some initial things to think about:      

Try thinking about simpler configurations first, such as those without trees.     

A one-dimensional version of the problem can be specified by setting W=2. Does this version of the problem have an exact solution?     

Packing equal circles into squares has been previously studied. See this reference for example. How might you use such information?
