options threads=6;
set
i /1*3/
m /1*12/
j /small,medium,large/
k /main,lux,student/
;
parameter
LP(i)
/1 100,2 200,3 300/
LE(i)
/1 0.5,2 0.6,3 0.8/
BD(i,k,j)
/1.main.small 100,1.main.medium 120,1.main.large 140,1.lux.small 90,1.lux.medium 100,1.lux.large 110,1.student.small 50,1.student.medium 70,1.student.large 120,2.main.small 40,2.main.medium 60,2.main.large 90,2.lux.small 55,2.lux.medium 75,2.lux.large 80,2.student.small 35,2.student.medium 65,2.student.large 85,3.main.small 65,3.main.medium 75,3.main.large 105,3.lux.small 20,3.lux.medium 35,3.lux.large 120,3.student.small 75,3.student.medium 85,3.student.large 95/
TOUG(i,j)
/1.small 10,1.medium 12,1.large 17,2.small 20,2.medium 24,2.large 28,3.small 15,3.medium 16,3.large 17/
TC(i)
/1 10,2 20,3 15/
;
*Static*
binary variables
CH(i)
IO(i,k,m)
CAP(i,j)
TOU(i,j)
;
positive variables
Q(i,k,m)
SA(i,k,m)
IN(i,k,m)
SH(i,k,m)
;
parameter
H(k)
/main 15,lux 10,student 5/
OC /1000/
SHC(k)
/main 10,lux 15,student 30/
MD(k,m)
/main.1 0.7,main.2 0.85,main.3 0.9,main.4 1,main.5 0.8,main.6 0.9,main.7 1,main.8 1.2,main.9 1.1,main.10 1.5,main.11 1.8,main.12 2
lux.1 1.1,lux.2 0.7,lux.3 0.8,lux.4 0.9,lux.5 0.7,lux.6 0.8,lux.7 0.9,lux.8 0.9,lux.9 1,lux.10 0.9,lux.11 1,lux.12 1.2
student.1 1.1,student.2 0.9,student.3 0.9,student.4 0.6,student.5 0.5,student.6 0.5,student.7 1.2,student.8 1.1,student.9 1,student.10 1,student.11 1,student.12 0.9/
SP(k)
/main 15,lux 30,student 12/
PC(k)
/main 5,lux 15,student 6/
CC(j)
/small 20,medium 30,large 40/
TOUC /50/
VC(k)
/main 4,lux 5,student 2/
VOL(j)
/small 100,medium 200,large 1400/
;
free variable z;
equation obj,TOU_L_CH,CAP_L_CH,IN_BALANCE,SELLING,ISORDER,CAPACITY,CHSH;
obj.. z=e=sum(i,(LP(i)*LE(i))*CH(i))+sum((i,j),CAP(i,j)*CC(j))+sum((i,j),TOU(i,j)*TOUC)-sum((i,k,m),SA(i,k,m)*SP(k))+sum((i,k,m),Q(i,k,m)*PC(k))+sum((i,k,m),IO(i,k,m)*(OC+TC(i)))+sum((i,k,m),IN(i,k,m)*H(k))+sum((i,k,m),SH(i,k,m)*SHC(k));
TOU_L_CH(i).. sum(j,TOU(i,j))=l=CH(i);
CAP_L_CH(i).. sum(j,CAP(i,j))=e=CH(i);
IN_BALANCE(i,k,m).. Q(i,k,m)+IN(i,k,m-1)-SA(i,k,m)=e=IN(i,k,m);
*initIN(i,k,m).. IN(i,k,m)=l=10000000$(ord(m) ne 1);
SELLING(i,k,m).. SA(i,k,m)+SH(i,k,m)=e=sum(j,MD(k,m)*BD(i,k,j)*CAP(i,j))+sum(j,(TOUG(i,j))*TOU(i,j));
ISORDER(i,k,m).. Q(i,k,m)=l=IO(i,k,m)*100000000;
CAPACITY(i,m).. sum(k,(Q(i,k,m)+IN(i,k,m))*VC(k))=l=sum(j,CAP(i,j)*VOL(j));
CHSH(i).. sum((k,m),SH(i,k,m))=l=CH(i)*10000000;
*CHQ(i).. sum((k,m),Q(i,k,m))=l=CH(i)*10000000;
*CHSA(i).. sum((k,m),SA(i,k,m))=l=CH(i)*10000000;
*CHIN(i).. sum((k,m),IN(i,k,m))=l=CH(i)*10000000;
*CHIO(i).. sum((k,m),IO(i,k,m))=l=CH(i)*10000000;
model Project /ALL/;
solve Project using mip minimizing z;
display z.l,CH.l,IN.l,Q.l,SH.l,SA.l,TOU.l,IO.l,CAP.l;
