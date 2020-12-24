# EasyQuizzer
Developed with the aim of helping teachers from different areas to create automated tests that are unique for each student. EasyQuizzer stores all questions in a database management system. Subsequently, an exam is set up with the desired items. The program will then run randomization algorithms that will shuffle both the questions and the alternatives so that there are no two identical exams.

SUMMARY/SUMÁRIO

ENGLISH VERSION
	INTRODUCTION
	INSTALLATION
		INSTALLATION-WINDOWS
		INSTALLATION-LINUX
	CONCLUSION


VERSÂO EM PORTUGUÊS do Brasil
	INTRODUÇÃO
	INSTALAÇÃO
		INSTALAÇÃO-WINDOWS
		INSTALAÇÃO-LINUX
	CONCLUSÃO	
	
================================================================================
ENGLISH VERSION
================================================================================
INTRODUCTION

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

INSTALLATION

INSTALLATION-WINDOWS
The primary requirement for installing EasyQuizzer is to have the System 
Database Manager (DBMS) Postgres 9.5 or higher version installed in the machine.
The page to download Postgres according to your system is this:

https://www.enterprisedb.com/downloads/postgres-postgresql-downloads

As we built the program in Java 8, then the Java JRE or JDK should also 
previously installed.

INSTALLATION-LINUX
Postgres DBMS usually is installed by default in most Linux distributions. 
An EasyQuizzer user should remember that if he will use the software on a Linux 
SO perhaps the installation of Postgres is not necessary any longer. In case of 
needing to install Postgres on Linux enter this command:

sudo apt install postgresql-9.5

Open versions of java are often installed on Linux systems.
If your system has OpenJDK version 8, then you do not have to install the 
Oracle's Java. In case you need to install OpenJDK enter this command:

sudo apt install openjdk-8-jdk

In the case of Linux, you also must install an additional library via the 
command:

sudo apt install openjfx

--

CONCLUSION
Guaranteed Java, Postgres, and openjfx (Linux only) installations, in theory, 
all that remains is to click on the file EasyQuizzer_vX.0.jar to start to enjoy 
the program or type java -jar EasyQuizzer_vX.0.jar at the command line, where X 
can be 1 or 2 version. 
The program has been tested successfully on the operating systems Window 7, 
Windows 10 and Linux Ubuntu 16.04 LTS.

If you encounter any problems, please contact the EasyQuizzer developers.

Enjoy it.

Anderson Santos


================================================================================
VERSÂO EM PORTUGUÊS do Brasil
================================================================================
INTRODUÇÃO

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

INSTALAÇÃO

INSTALAÇÃO-WINDOWS
O principal requisito para instalação do EasyQuizzer é ter previamente o Sistema
Gerenciador de Banco de Dados (SGBD) Postgres 9.5 ou superior instalado na
máquina. A página para baixar o Postgres de acordo com o seu sistema 
operacional é esta:

https://www.enterprisedb.com/downloads/postgres-postgresql-downloads

Como o programa foi feito em Java 8, então o JRE ou JDK da Java também deve 
estar previamente instalado.

INSTALAÇÃO-LINUX
Convêm lembrar que se o Easyquizzer vai ser utilizado em Linux é provável que
não seja necessária a instalação do Postgres porque esse SGBD costuma estar
instalado por padrão na maioria das distribuições Linux. Em caso de ser
necessário instalar o postgres no Linux basta digitar esse comando:

sudo apt install postgresql-9.5

Nos sistemas Linux também costuma estar instaladas versões abertas do java.
Se o seu sistema possui o openjdk versão 8 então você não precisa instalar o
java da Oracle. Para o caso de necessitar o openjdk basta digitar o comando:

sudo apt install openjdk-8-jdk

No caso do Linux, uma biblioteca adicional deve ser instalada via o comando: 

sudo apt install openjfx
--

CONCLUSÃO
Garantidas as instalações prévias da Java, do Postgres e openjfx (apenas Linux) 
em teoria tudo o que restá e clicar no arquivo EasyQuizzer_v1.0.jar para começar
 a usufruir do programa.
O programa foi testado com sucesso nos sistemas operacionas Window 7, Windows 10
 e Linux Ubuntu 16.04 LTS. 

Se encontrarem problemas que impeçam o uso favor entrar em contato com os 
desenvolvedores do EasyQuizzer.

Bom proveito.

Anderson Santos

e-mail: asantosbioinfo@users.sourceforge.net
