# Use uma imagem base com o JDK
FROM openjdk:17-jdk-slim

# Instale o Leiningen
RUN apt-get update && apt-get install -y curl && \
    curl -o /usr/local/bin/lein https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein && \
    chmod +x /usr/local/bin/lein && \
    lein

# Defina o diretório de trabalho
WORKDIR /app

# Copie os arquivos do projeto para o container
COPY . /app

# Instale as dependências do projeto
RUN lein deps

# Exponha a porta usada pela aplicação
EXPOSE 8080

# Comando para iniciar a aplicação
CMD ["lein", "run"]