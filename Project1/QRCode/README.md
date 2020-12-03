# MiniProjet 1 - QuickResponse Code Generation
*This submit is from Lam Christelle and Zammit Maxime*

These files contain :
- README.md
- DataEncoding.java
- MatrixConstruction.java (Part 1 & 2 + Bonus)
 

 Part 1 : Data Encoding


 Part 2 : Matrix Construction


 Part 3 : Data Placement in the Matrix


 Bonus :

 First Penalty Rule : We go all over the matrix and compare each byte with the next one. If they are identical, we add 1 to the counter. If one byte isn't identical to the following one, and if the counter is equal or superior to 5, we add the counter minus 2 to the penalty points total.

 Second Penalty Rule : We go all over the matrix and compare each byte with the next one, the one under it, and the one in diagonal. If they are all identical, we add 3 to the penalty points total. 

 Third Penalty Rule : We go all over the matrix and compare each byte with the beginning of the 2 sequences. If the first byte is similar to one of the sequences, we compare the rest. If it isn't identical, we break from the block and compare the next byte to the sequences.

 Fourth Penalty Rule : We go all over the matrix. For each byte, we add 1 to the number of total modules. For each black module, we add 1 to the number of black modules. We then calculate the percentage of black modules, and take the inferior and superior percentages, which are multiples of 5. We subtract 50 from both of the percentages, and take the absolute value. We multiply both of them by 2, and take the smallest one, which is added to the penalty points total.
 
 Mask Selection : We create the matrix and apply each mask. For every mask, we calculate the penalty points and compare them. The best mask is the one with the least penalty points. the matrix with the best mask is displayed.