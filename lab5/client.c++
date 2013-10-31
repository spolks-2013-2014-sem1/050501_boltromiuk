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
#include <signal.h>
#include <fcntl.h>
 
using namespace std;

#define BUFSIZE 1024 

void error(const char *msg)
{
    cout<<"\n"<<msg<<"\n";
    exit(0);
}

int connectUDP(uint port)
{   
    sockaddr_in server_addr;
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(port);
    if (inet_addr("127.0.0.1") != INADDR_NONE)     
        server_addr.sin_addr.s_addr = inet_addr("127.0.0.1");   
    int server = socket(AF_INET, SOCK_DGRAM, 0);  
    if(server<0)
        error("Socket error");
    if (connect(server, (sockaddr *)&server_addr, sizeof(server_addr)))
        error("Connect error");
    return server;
}

bool getFile(int sock,FILE *f,struct sockaddr_in from)
{  
    socklen_t fromlen=sizeof(from);
    int nbytes=0;
    long getBytes=0;
    char buf[BUFSIZE];
    while (1) {
        nbytes =recv(sock, buf, sizeof(buf), 0);     
        if (nbytes==1&&buf[0]=='a'){
            cout<<"\nDisconnect.Download comlete!\n";
            return true;
        }  
        if (nbytes < 0){cout<<"\nнезагрузил"; 
            return false;
}
        fwrite(buf,nbytes,1,f);
        getBytes+=nbytes; 
        if(getBytes%(1024*1024)==0)
            cout<<"\nMB get: "<<getBytes/(1024*1024);
        if(sendto(sock,"q",1,0,(struct sockaddr *) &from,fromlen)<0)
            return false;
    } 
}

int main(int argc, char *argv[])
{	
    if(argc!=2)
        error("Enter arguments: port"); 
    char buf[BUFSIZE],name[16],dparts[10];
    int dpart=0;
    struct sockaddr_in server,from;
    socklen_t serverlen=sizeof(server),fromlen;
    FILE *newfile,*oldfile;;
    int sock=connectUDP(atoi(argv[1]));
    if(send(sock,"client connected",16,0)<0)
        error("Connect eroor");
    if(recvfrom(sock, name, sizeof(name), 0,(struct sockaddr *) &server,&serverlen)<0)
        error("Get name error");
    oldfile=fopen(name,"r+");
    if(oldfile != NULL)  
    {
        cout<<"\nFile is found";
        while (!feof(oldfile)) {
            int b=fread(buf,1,sizeof(buf),oldfile);
            dpart++;
	}
        newfile=oldfile;
    }
    else  newfile = fopen(name, "w");
     dpart--;
    sprintf(dparts,"%i",dpart);
    if(sendto(sock,dparts,strlen(dparts),0,(struct sockaddr *) &server,serverlen )<0)
        error("Send download parts error");
    cout<<"\nDownload parts:"<<dpart;;
    if (newfile == NULL) 
        error("Create file error");
    long getBytes=0;
    if(!getFile(sock,newfile,server))
        error("Packet lost");
    fclose(newfile);
    close(sock);
    return 0;
}
