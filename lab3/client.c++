#include <iostream>
#include <netdb.h>
#include <sys/param.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <fstream>
#include <sys/types.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include <netdb.h>
#include <stdio.h>
using namespace std;

#define MST (-7)
void error(const char *msg)
{
    cout<<"\n"<<msg<<"\n";
    exit(0);
}

int main(int argc, char *argv[])
{	
    char buf[128];
    int server,dpart=0;
    FILE *newfile;
    FILE *oldfile;
    char name[16];
    server = socket(AF_INET, SOCK_STREAM, 0);
    if(server<0)error("Socket error");
    sockaddr_in dest_addr;
    dest_addr.sin_family = AF_INET;
    dest_addr.sin_port = htons(3000);
    if (inet_addr("127.0.0.1") != INADDR_NONE)     
        dest_addr.sin_addr.s_addr = inet_addr("127.0.0.1");  
    if (connect(server, (sockaddr *)&dest_addr, sizeof(dest_addr)))
        error("Connect erorr");
    recv( server, name, sizeof(name), 0 );
    oldfile=fopen(name,"r+");
    if(oldfile != NULL)  
    {
        cout<<"naiden\n";
        while (!feof(oldfile)) {
            int b=fread(buf,1,sizeof(buf),oldfile);
            int size=ftell(oldfile);
            cout<<"\rbytes read:"<<b<<", part:"<<dpart<<", pos:"<<size;
            dpart++;
	}
        newfile=oldfile;
    }
    else  newfile = fopen(name, "w");
    char dparts[10];
    sprintf(dparts,"%i",dpart);
    send(server,dparts,strlen(dparts),0);
    cout<<"\nDownload parts:"<<dpart<<"\n"<<endl;
    if (newfile == NULL) 
        error("Create file error");
    int i=0;
    while (1) {
        int nbytes = recv( server, buf, sizeof(buf), 0 );
        if ( nbytes == 0)
            error("Disconnect.Download comlete!");
        if (nbytes < 0)
            error("Not get byte");
        fwrite(buf,nbytes,1,newfile);
    	cout<<"\rBytes:"<<nbytes<<", "<<"part: "<<i;
    	i++; 
    }
    fclose(newfile);
    fclose(oldfile);
    close(server);
    return 0;
}
