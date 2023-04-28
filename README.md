# EasyQuizzer
Developed with the aim of helping teachers from different areas to create automated tests that are unique for each student. EasyQuizzer stores all questions in a database management system. Subsequently, an exam is set up with the desired items. The program will then run randomization algorithms that will shuffle both the questions and the alternatives so that there are no two identical exams.

SUMMARY/SUMÁRIO

- ENGLISH VERSION
	- INTRODUCTION
	- INSTALLATION
		- INSTALLATION-WINDOWS
		- INSTALLATION-LINUX
	- CONCLUSION


- VERSÂO EM PORTUGUÊS do Brasil
	- INTRODUÇÃO
	- INSTALAÇÃO
		- INSTALAÇÃO-WINDOWS
		- INSTALAÇÃO-LINUX
	- CONCLUSÃO	
	

# ENGLISH VERSION - INTRODUCTION

This software aims to facilitate the work of educators worldwide. With 
EasyQuizzer you can create a question's databases of right and wrong 
alternatives per item. EasyQuizzer ensures that every student in the classroom 
will have a unique copy of each issue because the options are exchanged (changed
from order) and combined (different groups of alternatives). For example, 
if you created a question with five wrong and two right options, EasyQuizzer may
generate up to one hundred and twenty (120) different versions of the same issue,
with their respective templates.

If all questions have the same number of right and wrong alternatives, then you 
can create 120 different tests. However, the user of the EasyQuizzer is who 
defines how many right and wrong alternatives each question will have.

The user should be aware of the fact that if any question has some number of 
alternatives smaller than the others, then a test with this question will be 
limited to the number of variations of this issue with the lower number of 
possibilities.

It is also possible to include open questions together with closed questions, 
but these do not limit the number of tests that EasyQuizzer will generate.

This first version was coded only for the Portuguese language of Brazil but this
second version includes English as a possible setup. Even so, a database created
by version 1.0 is compatible with version 2.0 and vice-versa.

--

# INSTALLATION

## INSTALLATION-WINDOWS

The primary requirement for installing EasyQuizzer is to have the System 
Database Manager (DBMS) Postgres 10 or higher version installed in the machine.
The page to download Postgres according to your system is this:

https://www.enterprisedb.com/downloads/postgres-postgresql-downloads

As we built the program in Java 8, then the Java JRE or JDK should also 
previously installed.

## INSTALLATION-LINUX

Postgres DBMS usually is installed by default in most Linux distributions. 
An EasyQuizzer user should remember that if he will use the software on a Linux 
SO perhaps the installation of Postgres is not necessary any longer. In case of 
needing to install Postgres on Linux enter this command:

```bash
sudo apt install postgresql
```
We need access to the Postgres database administrator user without necessarily logging in to the Postgres user of the operating system (OS). For this, we must change the password of the Postgres user of the OS:

```bash 
sudo passwd postgres 
``` 
Once the password is changed, we need to log in with the Postgres user in the OS:

```bash 
su postgres 
``` 
As the OS Postgres user, we need to change the password of the Postgres database user. Note that they are distinct users with the same name: the OS and DBMS users are called "postgres". To change the password of the DBMS Postgres user, we first call the psql command:

```bash 

psql 
``` 
No password is requested as we are logged in with the OS user who administers the DBMS. Once the psql interface is opened, we can change the password of the Postgres user of the DBMS with the command: 

```sql  

postgres=# ALTER USER postgres WITH ENCRYPTED PASSWORD 'your-postgres-password'; 

postgres=# \q 

``` 

If the above command was successful, you could close the DBMS session and your OS Postgres user: 

```bash 
exit 
``` 
... back to your Ubuntu OS admin user. Now you can run the command below without necessarily being the Postgres user of the OS:

```bash 
psql -h localhost -U postgres -p 5432 -c "\du" 
``` 

The above command should return the users registered in your database. As you have just installed Postgres, only the user "postgres" should be in your DBMS. 

Open versions of java are often installed on Linux systems. However,
the default openjfx package on Ubuntu is no longer compatible with OpenJDK 8. 
You may use the older version of the openjfx package installed with some
specific java binaries. This post explains several possibilities to proceed.

https://stackoverflow.com/questions/61783369/install-openjdkopenjfx-8-on-ubuntu-20

I adopted the below solution. 


```bash
curl -s "https://get.sdkman.io" | bash

source "$HOME/.sdkman/bin/sdkman-init.sh"

sdk install java 8.0.352.fx-zulu
```

Depending on your operational system, the package name varies. A simple search within 
the repository can show you several possibilities.

--

# CONCLUSION

Guaranteed Java, Postgres, and openjfx (Linux only) installations, in theory, 
all that remains is to click on the file EasyQuizzer_vX.0.jar to start to enjoy 
the program or type java -jar EasyQuizzer_vX.0.jar at the command line, where X 
can be 1 or 2 version. 
The program has been tested successfully on the operating systems Window 7, 
Windows 10 and Linux Ubuntu 18.04 LTS and 20.04 LTS.

If you encounter any problems, please contact the EasyQuizzer developers.

Enjoy it.

Anderson Santos



# VERSÂO EM PORTUGUÊS- INTRODUÇÃO

Este software têm como objetivo facilitar o trabalho de educadores mundo afora.
Com o EasyQuizzer será possível criar bancos de dados de questões e alterna-
tivas certas e erradas por questões. Com um conjunto de alternativas certas e
erradas o EasyQuizzer garante que cada aluno em sala de aula terá uma versão 
exclusiva de cada questão porque as alternativas são permutadas (trocadas de 
ordem) e combinadas (grupos diferentes de alternativas). Por exemplo, se uma 
questão for cadastrada com 5 alternativas erradas e 2 certas o EasyQuizzer 
poderá gerar até cento e vinte (120) versões diferentes de uma mesma questão,
com seus respectivos gabaritos.

Se todas as questões tiverem essa quantidade de alternativas certas e erradas 
então é possível criar 120 provas diferentes. Entretanto, o usuário do 
EasyQuizzer é que define quantas alternativas certas e erradas cada questão 
terá.

O usuário deve estar atento ao fato que se alguma questão possuir uma quantidade
menor de alternativas do que as demais então uma prova com essa questão fica 
limitada à quantidade de variações dessa questão com menor quantidade de 
alternativas.

Também é possível incluir questões abertas junto com questões fechadas, mas 
essas não limitam a quantidade de provas possíveis de serem geradas.

A primeira versão foi codificada apenas para a linguagem Português
do Brasil. A segunda versão possui opção de exibir a linguagem Inglês ou 
Português.

--

# INSTALAÇÃO

## INSTALAÇÃO-WINDOWS

O principal requisito para instalação do EasyQuizzer é ter previamente o Sistema
Gerenciador de Banco de Dados (SGBD) Postgres 10 ou superior instalado na
máquina. A página para baixar o Postgres de acordo com o seu sistema 
operacional é esta:

https://www.enterprisedb.com/downloads/postgres-postgresql-downloads

Como o programa foi feito em Java 8, então o JRE ou JDK da Java também deve 
estar previamente instalado.

## INSTALAÇÃO-LINUX

Convêm lembrar que se o Easyquizzer vai ser utilizado em Linux é provável que
não seja necessária a instalação do Postgres porque esse SGBD costuma estar
instalado por padrão na maioria das distribuições Linux. Em caso de ser
necessário instalar o postgres no Linux basta digitar esse comando:

```bash
sudo apt install postgresql
```

Precisamos ter acesso ao usuário administrador de banco de dados do postgres sem necessariamente fazermos o login no usuário postgres do sistema operacional (SO). Para isto, temos que alterar a senha do usuário postgres do SO: 
```bash 
sudo passwd postgres 
``` 

Uma vez alterada a senha precisamos logar com o usuário postgres no SO: 
```bash 
su postgres 
``` 

Como o usuário postgres do SO, precisamos alterar a senha do usuário de banco de dados do postgres. Perceba que são usuários distintos com o mesmo nome: o usuário do SO e o do SGBD se chamam postgres. Para alterar a senha do usuário postgres do SGBD, primeiro chamamos o comando psql: 

```bash 
psql 
``` 

Como estamos logados com o usuário do SO que administra o SGBD não é solicitada nenhuma senha. Uma vez aberta a interface do psql podemos alterar a senha do usuário postgres do SGBD com o comando: 

```sql  
postgres=# ALTER USER postgres WITH ENCRYPTED PASSWORD 'sua-senha-postgres'; 

postgres=# \q 

``` 

Se o comando acima foi bem-sucedido, você pode fechar a sessão do SGBD e do seu usuário postgres do SO: 

```bash 
exit 
``` 
... voltando a seu usuário administrador do SO Ubuntu. Agora você consegue executar o comando abaixo sem necessariamente estar no usuário postgres do SO: 

```bash 
psql -h localhost -U postgres -p 5432 -c "\du" 
``` 

O comando acima deve retornar os usuários cadastrados no seu banco de dados. Como você acabou de instalar o postgres deve haver apenas o usuário "postgres" no seu SGBD.

Nos sistemas Linux também costumam estarem instaladas versões abertas do java.
No entanto, o pacote openjfx padrão no Ubuntu não é mais compatível com o OpenJDK 8.
Você pode usar a versão mais antiga do pacote openjfx instalado com alguns
binários java específicos. Este post explica várias possibilidades para prosseguir.

https://stackoverflow.com/questions/61783369/install-openjdkopenjfx-8-on-ubuntu-20

Adotei a solução abaixo.

```bash
curl -s "https://get.sdkman.io" | bash

source "$HOME/.sdkman/bin/sdkman-init.sh"

sdk install java 8.0.352.fx-zulu
```

Dependendo do seu sistema operacional, o nome do pacote varia. Uma simples pesquisa 
no repositório pode mostrar várias possibilidades.

--

# CONCLUSÃO

Garantidas as instalações prévias da Java, do Postgres e openjfx (apenas Linux) 
em teoria tudo o que restá e clicar no arquivo EasyQuizzer_v1.0.jar para começar
a usufruir do programa.
O programa foi testado com sucesso nos sistemas operacionas Window 7, Windows 10
e Linux Ubuntu 18.04 LTS e 20.04 LTS. 

Se encontrarem problemas que impeçam o uso favor entrar em contato com os 
desenvolvedores do EasyQuizzer.

Bom proveito.

Anderson Santos

