# Agenda de contato

Essa agenda foi desenvolvida com play framework 2.6 com hibernate JPA e MySQL. Utilizou-se também Bootstrap.

#Funcionalidades
- Sistema de Login (apenas usuário logados podem inserir contatos)
- Inserir, editar e apagar contatos na agenda (foto, nome, telefone, idade, dataNascimento, email)
- Criar grupos (Ex: Amigos, familiáres, Futebol, Condomínio)
- Lista contatos e grupos registrados

Melhorias importantes a ser feitas: verificações dos formulários, ajustes visuais e busca. 

#Pré-requisitos para rodar a aplicação

- [Java SE 1.8 ou superior](https://www.oracle.com/br/java/technologies/javase/javase8-archive-downloads.html)
- [sbt](https://www.scala-sbt.org/download.html)

Recomenda-se utilizar uma IDE. O projeto foi desenvolvido com InteliJ.

#Como rodar a aplicação

Antes de instalar as dependencias crie uma tabela no bando de dados MySQL com o nome "playcontactbookdb". Abra o arquivo conf/application.conf e configure seu banco de dados. O projeto está pré-configurado para o Xampp, porta 3306, em um banco de dados sem senha. Feito isso, siga os seguintes passos:

1 - Abra um terminal na basta do projeto

2 - Digite: sbt update

3 - Digite: sbt

4 - Digite: compile

5 - Na raiz do projeto digite: sbt run

6 - Depois que a mensagem Server started, ... for exibida, insira o seguinte URL em um navegador: http: // localhost: 9000


#Dicas extras

Para quem não está habituado com o desenvolvimento de aplicações Java, saiba que você precisa configurar suas variáveis de ambiente. Em Path adicione o caminho da pasta bin do java (ex: C:\Program Files\Java\jdk1.8.0_301\bin). Crie JAVA_HOME e adicione o caminho do java (ex: C:\Program Files\Java\jdk1.8.0_202).

Existe uma grande chance de conflitos com versões posteriores do Java, evite dificuldades utilizando a versão 1.8.
