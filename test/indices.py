import matplotlib.pyplot as plt
import numpy

from math import factorial

def binom(a,b):
    return factorial(a) / (factorial(b)*factorial(a-b))

def stirling(n,k):
    if n<=0 or n!=0 and n==k:
        return 1
    elif k<=0 or n<k:
        return 0
    elif n==0 and k==0:
        return -1
    else:
        s = sum((-1)**(k-j)*binom(k,j)*j**n for j in range(k+1))
        return s / factorial(k)

log = []
with open("indices.log") as indices:
    next(indices)
    for line in indices:
        indices = line.split()[1:7]
        size = len(set(indices))
        log.append(size)

stirlings = numpy.array([stirling(6, k) for k in range(1,7)])
plt.hist(log, [0.5,1.5,2.5,3.5,4.5,5.5,6.5,7.5])
plt.plot(range(1,7), stirlings * len(log)/stirlings.sum())
plt.show()
