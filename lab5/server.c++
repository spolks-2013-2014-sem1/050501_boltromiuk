#include <sys/types.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include <netdb.h>
#include <stdio.h>
#include <arpa/inet.h>
#include <iostream>

using namespace std;

#define BUFSIZE 1024

void error(const char *msg)
{
    cout<<"\n"<<msg<<"\n";
    exit(0);
}
int connectUDP(uint port)
{  
    struct sockaddr_in server;
    int sock=socket(AF_INET, SOCK_DGRAM, 0);
    if (sock < 0) 
        error("Opening socket");
    bzero(&server,sizeof(server));
    server.sin_family=AF_INET;
    server.sin_addr.s_addr = INADDR_ANY;
    server.sin_port=htons(port);
    if (bind(sock,(struct sockaddr *)&server,sizeof(server))<0) 
        error("Binding error");
    return sock;
}

bool sendFile(int sock,FILE *f,int dpart,struct sockaddr_in from)
{
    socklen_t fromlen=sizeof(from);
    char buf[BUFSIZE];
    int n=0,b=0,size=0;
    while (!feof(f)) {
        b=fread(buf,1,sizeof(buf),f);
        if(b!=0&&dpart<=0){
            n=sendto(sock,buf,b,0,(struct sockaddr *) &from,fromlen);
            if(n<0)
                return false;
            n=recv(sock, buf, 1, 0);
            if (buf[0]!='q')
                return false;
        }
        dpart--;
        size+=b;
        if(size%(1024*1024)==0&&dpart<=0)
            cout<<"\nMB send:"<<size/(1024*1024);
   }
   return true;
}

int main(int argc, char *argv[])
{
    if(argc!=3)
        error("Enter arguments: port filepath");
    char *name=argv[2]; 
    struct sockaddr_in from;
    socklen_t fromlen=sizeof(from);
    FILE *f=fopen(name,"r");
    if(f==NULL)
        error("Cant open file");
    char buf[BUFSIZE];
    int sock=connectUDP(atoi(argv[1]));
    while(1){  
        bzero(&from,sizeof(from));
        printf("\nWait connect\n");
        recvfrom(sock, buf, 16, 0,(struct sockaddr *) &from,&fromlen);
        cout<<buf;
        sendto(sock,basename(name),sizeof(basename(name)),0,(struct sockaddr *) &from,fromlen);
        recv(sock, buf, sizeof(buf), 0);       
        int dpart=atoi(buf);
        cout<<"\nDownload parts:"<<dpart; 
        fseek(f, 0L, SEEK_END);
        cout<<(ftell(f)/BUFSIZE);
        if(dpart!=(ftell(f)/BUFSIZE)){   
        fseek(f, 0L, SEEK_SET);
        if(!sendFile(sock,f,dpart,from))
        {
            cout<<"\nPacket lost";
            continue;
        }}
        cout<<"\nDownload compleat";
        sendto(sock,"a",1,0,(struct sockaddr *) &from,fromlen);
        
    }
    close(sock);
    fclose(f);
    return 0;
}
