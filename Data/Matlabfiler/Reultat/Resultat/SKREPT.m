clc 
clear all
clear 

Q=exp(12.7);

Atlcent = zeros(584,15);
Bisc = zeros(584,15);
Celtic = zeros(584,15);
Gron = zeros(584,15);
Gul = zeros(584,15);
Iceland = zeros(584,15);
Kust = zeros(584,15);
NorthSea = zeros(584,15);
NorwegianSea = zeros(584,15);
Rod = zeros(584,15);
Alla = zeros(584,15,10);


for k=0:14
strtmp = strcat('NetworkTest-AtlanticCenter-waveH-',int2str(k));
str = strcat(strtmp,'.txt');
Atlcent(:,k+1) = importdata(str)*Q; 
Alla(:,k+1,1)  = importdata(str)*Q; 
end


for k=0:14
strtmp = strcat('NetworkTest-Biscay-waveH-',int2str(k));
str = strcat(strtmp,'.txt');
Bisc(:,k+1) = importdata(str)*Q; 
Alla(:,k+1,2)  = importdata(str)*Q; 
end

for k=0:14
strtmp = strcat('NetworkTest-CelticShelf-waveH-',int2str(k));
str = strcat(strtmp,'.txt');
Celtic(:,k+1) = importdata(str)*Q; 
Alla(:,k+1,3)  = importdata(str)*Q; 
end

for k=0:14
strtmp = strcat('NetworkTest-Groenlandsudden-waveH-',int2str(k));
str = strcat(strtmp,'.txt');
Gron(:,k+1) = importdata(str)*Q; 
Alla(:,k+1,4)  = importdata(str)*Q; 
end

for k=0:14
strtmp = strcat('NetworkTest-Gul-waveH-',int2str(k));
str = strcat(strtmp,'.txt');
Gul(:,k+1) = importdata(str)*Q; 
Alla(:,k+1,5)  = importdata(str)*Q; 
end

for k=0:14
strtmp = strcat('NetworkTest-Iceland-waveH-',int2str(k));
str = strcat(strtmp,'.txt');
Iceland(:,k+1) = importdata(str)*Q; 
Alla(:,k+1,6)  = importdata(str)*Q; 
end
for k=0:14
strtmp = strcat('NetworkTest-Kust-waveH-',int2str(k));
str = strcat(strtmp,'.txt');
Kust(:,k+1) = importdata(str)*Q; 
Alla(:,k+1,7)  = importdata(str)*Q; 
end

for k=0:14
strtmp = strcat('NetworkTest-NorthSea-waveH-',int2str(k));
str = strcat(strtmp,'.txt');
NorthSea(:,k+1) = importdata(str)*Q; 
Alla(:,k+1,8)  = importdata(str)*Q; 
end
for k=0:14
strtmp = strcat('NetworkTest-NorwegianSea-waveH-',int2str(k));
str = strcat(strtmp,'.txt');
NorwegianSea(:,k+1) = importdata(str)*Q; 
Alla(:,k+1,9)  = importdata(str)*Q; 
end
for k=0:14
strtmp = strcat('NetworkTest-Rod-waveH-',int2str(k));
str = strcat(strtmp,'.txt');
Rod(:,k+1) = importdata(str)*Q; 
Alla(:,k+1,10)  = importdata(str)*Q; 
end

LugnDag = zeros(584,15);

str = 'NetworkTest-Rod-waveH-0.txt'
LugnDag = importdata(str)*Q; 

HomeCol = [1 0 0; 0.7 0 0.3; 0.5 0 0.5; 0.3 0 0.7; 0 0 1; 0 0.3 0.7; 0 0.5 0.5; 0 0.7 0.3; 0 1 0; 0 0 0];  


%%

a=10
clc 
clf
hold on
for i=1:a
  fest = polyfit(1:584,(1000*Alla(:,10,i))',99)
  subplot(5,2,i)
  hold on
  plot(1000*(Alla(:,10,i)),'Color',HomeCol(i,:));
  f = polyval(fest,1:584);
  plot(1:584,f,'-')
  axis([0 300 0 100])
    
end


hold off

%%
HomeCol = [1 0 0; 0.7 0 0.3; 0.5 0 0.5; 0.3 0 0.7; 0 0 1; 0 0.3 0.7; 0 0.5 0.5; 0 0.7 0.3; 0 1 0; 0 0 0];  
a=1

clc 
clf

hold on
for i=1:10
   
   [sp, val] = spaps(linspace(30,300,584),20*log10(Alla(:,12,i)),5200)
   %plot(linspace(30,300,584),20*log10(Alla(:,12,i)),'*','Color',HomeCol(i,:),'MarkerSize',2);
   plot(linspace(30,300,584),val,'Color',HomeCol(i,:)); 
  
 
    
end


hold off

%%
clc 
clf
% Biscaya, norwegian, Lugndag
set(gcf,'renderer','painters')
hold on
for i=3:3
    [sp1, val1] = spaps(linspace(30,300,584),10*log10(Alla(:,4*i+1,2)),400);
    H1 = plot(linspace(30,300,584),10*log10(Alla(:,4*i+1,2)),'.','Color',[(i-1)/2 0 0], 'MarkerSize', 6); 
    H2 = plot(linspace(30,300,584),val1,'Color',[(i-1)/2 0 0],'LineWidth',1); 
end
for i=3:3
     [sp2, val2] = spaps(linspace(30,300,584),10*log10(Alla(:,4*i+1,9)),400);
     H3 = plot(linspace(30,300,584),10*log10(Alla(:,4*i+1,9)),'.','Color',[0 (i-1)/3.5 0], 'MarkerSize', 6); 
     H4 = plot(linspace(30,300,584),val2,'Color',[0 (i-1)/3.5  0],'LineWidth',1); 
end
 [sp3, val3] = spaps(linspace(30,300,584),10*log10(LugnDag(:)),400);
    H5 = plot(linspace(30,300,584),10*log10(LugnDag(:)),'.','Color',[0 0 0], 'MarkerSize', 6); 
 H6 = plot(linspace(30,300,584),val3,'Color',[0 0  0],'LineWidth',1); 

 
 axis([30 300 -60 60])
xlabel('Frekvens(mHz)','interpreter','latex','fontsize',14)
ylabel('Logaritmerad relativ Effekt','interpreter','latex','fontsize',14)
grid on
set(gca,'xtick',[25:25:300])
set(gca,'ytick',[-60:20:60])
leg1 = legend([H1 H3 H5], 'Biscaya, 12m','Norska havet, 12m','referensdag')
set(leg1,'Interpreter','latex','fontsize',10)
hold off

print(gcf, '-depsc2', '~/Nor12Bis12Lugn.eps')
%%
clc 
clf



   
  
plot(linspace(30,300,584),20*log10(LugnDag(:)),'Color',[0 0 0]); 
   
title('LugnDag')
legend('0m')


%%
clc
clf
set(gcf,'renderer','painters')
CullorMapp = importdata('~/Git/Huvudprogram/MEGASAVEFILE8.txt');


for m=1:85
    for n=2:35
        CullorMapp(m,n) = log(CullorMapp(m,n));
            
    
            
    end
end
        
   

imagesc(flipud(CullorMapp(:,2:35)'));
axis off
axis equal
axis tight
colorbar
ylabel(colorbar,'Mikroseismikintensitet', 'interpreter', 'latex', 'fontsize', 14)

print(gcf, '-depsc2', '~/Cullormapp.eps')