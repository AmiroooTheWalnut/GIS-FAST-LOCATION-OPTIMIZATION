options threads=6;
set
i /1*22/
m /1*12/
j /small,medium,large/
k /main,lux,student/;
parameter
LP(i)
/1 8.1,2 7.8,3 9.1,4 5.7,5 5.0,6 7.8,7 5.8,8 4.6,9 2.6,10 1.8,11 2.9,12 2.5,13 4.2,14 1.5,15 2.2,16 1.5,17 1.8,18 1.8,19 1.8,20 1.5,21 2.3,22 3.4/
LE(i)
/1 0.6,2 0.2,3 0.2,4 0.2,5 0.4,6 0.2,7 0.4,8 0.2,9 0.4,10 0.2,11 0.2,12 0.2,13 0.6,14 0.2,15 0.2,16 0.8,17 0.8,18 1.0,19 0.8,20 0.8,21 0.6,22 0.8/
BD(i,k,j)
/1.main.small 0.0,1.main.medium 0.0,1.main.large 0.0,1.lux.small 0.0,1.lux.medium 0.0,1.lux.large 0.0,1.student.small 0.0,1.student.medium 0.0,1.student.large 0.0,2.main.small 0.0,2.main.medium 0.0,2.main.large 0.0,2.lux.small 0.0,2.lux.medium 0.0,2.lux.large 0.0,2.student.small 39.987453,2.student.medium 40.997326,2.student.large 40.63826,3.main.small 0.0,3.main.medium 0.0,3.main.large 0.0,3.lux.small 0.0,3.lux.medium 0.0,3.lux.large 0.0,3.student.small 0.0,3.student.medium 0.0,3.student.large 0.0,4.main.small 0.0,4.main.medium 0.0,4.main.large 0.0,4.lux.small 0.0,4.lux.medium 0.0,4.lux.large 0.0,4.student.small 0.0,4.student.medium 0.0,4.student.large 0.0,5.main.small 0.0,5.main.medium 0.0,5.main.large 0.0,5.lux.small 0.0,5.lux.medium 0.0,5.lux.large 0.0,5.student.small 0.0,5.student.medium 0.0,5.student.large 0.0,6.main.small 0.0,6.main.medium 0.0,6.main.large 0.0,6.lux.small 0.0,6.lux.medium 0.0,6.lux.large 0.0,6.student.small 0.0,6.student.medium 0.0,6.student.large 0.0,7.main.small 0.0,7.main.medium 0.0,7.main.large 0.0,7.lux.small 2.499,7.lux.medium 3.8422127,7.lux.large 3.8318,7.student.small 3036.348,7.student.medium 3037.7383,7.student.large 3037.4072,8.main.small 83.28914,8.main.medium 83.46841,8.main.large 83.43154,8.lux.small 1389.3802,8.lux.medium 1389.7267,8.lux.large 1389.8314,8.student.small 10969.902,8.student.medium 10970.808,8.student.large 10970.97,9.main.small 0.0,9.main.medium 0.0,9.main.large 0.0,9.lux.small 0.0,9.lux.medium 0.0,9.lux.large 0.0,9.student.small 0.0,9.student.medium 0.0,9.student.large 0.0,10.main.small 0.0,10.main.medium 0.0,10.main.large 0.0,10.lux.small 120.534424,10.lux.medium 121.13134,10.lux.large 122.15803,10.student.small 20855.54,10.student.medium 20855.754,10.student.large 20856.018,11.main.small 0.0,11.main.medium 0.0,11.main.large 0.0,11.lux.small 0.0,11.lux.medium 0.0,11.lux.large 0.0,11.student.small 1736.8369,11.student.medium 1737.2883,11.student.large 1737.7708,12.main.small 4.2835937,12.main.medium 4.4258657,12.main.large 4.5059958,12.lux.small 29.781874,12.lux.medium 30.2332,12.lux.large 30.865055,12.student.small 303.72202,12.student.medium 304.09766,12.student.large 304.64362,13.main.small 0.0,13.main.medium 0.0,13.main.large 0.0,13.lux.small 7.545916,13.lux.medium 7.6755996,13.lux.large 7.648927,13.student.small 21.733147,13.student.medium 21.873543,13.student.large 21.844667,14.main.small 0.0,14.main.medium 0.0,14.main.large 0.0,14.lux.small 0.0,14.lux.medium 0.0,14.lux.large 0.0,14.student.small 0.0,14.student.medium 0.0,14.student.large 0.0,15.main.small 5.835914,15.main.medium 6.1418796,15.main.large 6.9237914,15.lux.small 618.90454,15.lux.medium 619.1789,15.lux.large 619.88,15.student.small 2393.2993,15.student.medium 2393.5686,15.student.large 2394.2568,16.main.small 0.0,16.main.medium 0.0,16.main.large 0.0,16.lux.small 0.0,16.lux.medium 0.0,16.lux.large 0.0,16.student.small 0.0,16.student.medium 0.0,16.student.large 0.0,17.main.small 0.99028283,17.main.medium 1.3987745,17.main.large 1.8980421,17.lux.small 39.7149,17.lux.medium 40.005463,17.lux.large 40.36059,17.student.small 1035.9382,17.student.medium 1036.2825,17.student.large 1036.948,18.main.small 0.0,18.main.medium 0.0,18.main.large 0.0,18.lux.small 0.0,18.lux.medium 0.0,18.lux.large 0.0,18.student.small 0.0,18.student.medium 0.0,18.student.large 0.0,19.main.small 0.0,19.main.medium 0.0,19.main.large 0.0,19.lux.small 0.0,19.lux.medium 0.0,19.lux.large 0.0,19.student.small 0.0,19.student.medium 0.0,19.student.large 0.0,20.main.small 0.0,20.main.medium 0.0,20.main.large 0.0,20.lux.small 0.0,20.lux.medium 0.0,20.lux.large 0.0,20.student.small 8.276868,20.student.medium 8.696339,20.student.large 9.033561,21.main.small 0.0,21.main.medium 0.0,21.main.large 0.0,21.lux.small 0.0,21.lux.medium 0.0,21.lux.large 0.0,21.student.small 0.0,21.student.medium 0.0,21.student.large 0.0,22.main.small 0.0,22.main.medium 0.0,22.main.large 0.0,22.lux.small 0.0,22.lux.medium 0.0,22.lux.large 0.0,22.student.small 0.0,22.student.medium 0.0,22.student.large 0.0/
TC(i)
/1 1.0607419,2 0.58198315,3 0.94838417,4 0.7987667,5 0.54843384,6 0.44043055,7 0.51995367,8 0.5079641,9 0.21549074,10 0.28173122,11 0.5915388,12 0.5051026,13 0.56621325,14 0.843132,15 0.85801613,16 0.6088318,17 0.36320812,18 0.42331037,19 0.63682145,20 0.6118139,21 0.33452716,22 0.38286164/
TOUG(i,j)
/1.small 0.0,1.medium 0.0,1.large 0.0,2.small 0.0,2.medium 0.0,2.large 39.987453,3.small 0.0,3.medium 0.0,3.large 0.0,4.small 0.0,4.medium 0.0,4.large 0.0,5.small 0.0,5.medium 0.0,5.large 0.0,6.small 0.0,6.medium 0.0,6.large 0.0,7.small 0.0,7.medium 2.499,7.large 3036.348,8.small 83.28914,8.medium 1389.3802,8.large 10969.902,9.small 0.0,9.medium 0.0,9.large 0.0,10.small 0.0,10.medium 120.534424,10.large 20855.54,11.small 0.0,11.medium 0.0,11.large 1736.8369,12.small 4.2835937,12.medium 29.781874,12.large 303.72202,13.small 0.0,13.medium 7.545916,13.large 21.733147,14.small 0.0,14.medium 0.0,14.large 0.0,15.small 5.835914,15.medium 618.90454,15.large 2393.2993,16.small 0.0,16.medium 0.0,16.large 0.0,17.small 0.99028283,17.medium 39.7149,17.large 1035.9382,18.small 0.0,18.medium 0.0,18.large 0.0,19.small 0.0,19.medium 0.0,19.large 0.0,20.small 0.0,20.medium 0.0,20.large 8.276868,21.small 0.0,21.medium 0.0,21.large 0.0,22.small 0.0,22.medium 0.0,22.large 0.0/;
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
SELLING(i,k,m).. SA(i,k,m)+SH(i,k,m)=e=sum(j,MD(k,m)*BD(i,k,j)*CAP(i,j))+sum(j,MD(k,m)*BD(i,k,j)*(1+TOUG(i,j))*TOU(i,j));
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
