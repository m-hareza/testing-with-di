# testing-with-di

Simple project showing how dependency injection helps in:

1. testing code
2. separating responsibilities 


There are 4 versions (v1, v2, v3, v4). In the first version all the responsibilities, calculating date and its synchronization, lies on TaskPeriodSynchronizer 
class. Thus, task period synchronizer as well as its tests are complicated. Testing of calculation requires to fulfil all requirements set out by synchronizer 
and vice versa. Second version extracts calculation logic from TaskPeriodSynchronizer. It allows developer to test calculation separately. Nevertheless 
developer still need to meet calculator requirements to test synchronizer logic. Version number 3 engages dependency injection which allows him to stub 
calculator and get rid off calculator logic in synchronizer tests. Calculator factory instead of the calculator itself is injected in the last version.


