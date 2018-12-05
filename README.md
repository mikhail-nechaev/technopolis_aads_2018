# AaDS

Homework for Algorithms and Data structures on Java for students [Technopolis](https://polis.mail.ru/pages/index/) in [Peter the Great St.Petersburg Polytechnic University](https://english.spbstu.ru).

---
**Deadline 18.12.2018**

In all classes need implement all required methods.

Before pull request make sure than relevant tests is passed.

---
| Points | Iterator remove | Class | Test |
| ------ |-----------------| ----- | ---- |
| 2.5    | +1 point TestIDequeRemove | ArrayDequeSimple | TestIDeque
| 2.5    | +1 point TestIDequeRemove | LinkedDequeSimple | TestIDeque
| 2.5    | +1 point        | ArrayPriorityQueueSimple | TestIPriorityQueue
| 2.5    | -               | MergingPeekingIncreasingIterator + IntegerIncreasingSequencePeekingIterator | TestIntegerIncreasingSequencePeekingIterator + TestMergingPeekingIncreasingIterator 
| 7.5    | -               | ArrayDequeFull | TestIDeque + TestFullDeque
| 7.5    | -               | LinkedDequeFull | TestIDeque + TestFullDeque
| 5.0    | -               | AVLTree | --
| 5.0    | +1 point        | AVLTreeIterable | --
| 5.0    | -               | RedBlackTree (add) | --
| 5.0    | -               | RedBlackTree (add + remove) | --
| 5.0    | +1 point        | OpenHashTable + Student | -- |
| =50    | =5 | |

---
**How to pull changes from master inside your fork**

Make once:

`git remote add base https://github.com/mikhail-nechaev/technopolis_aads_2018.git`

or

`git remote add base git@github.com:mikhail-nechaev/technopolis_aads_2018.git`
 
and then

`git pull base master`

for more information [see 2.5 Git Basics - Working with Remotes](https://git-scm.com/book/en/v2/Git-Basics-Working-with-Remotes)