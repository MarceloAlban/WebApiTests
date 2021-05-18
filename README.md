# WebApiTests
Servidor simples para testes de api mockadas

Modo de usar:
- Adicione suas rotas dentro do arquivo Application.conf
  - Ao adicionar a rota, será preciso informar alguns dados:
    "nome;method;file/json;delay"
    
    - nome= Nome do endpoint
    - tipo de metodo= Get, Post, Put ou Delete
    - file/json= Arquivo de retorno ou json (Em caso de arquivo, adicionar o mesmo dentro da pasta "resources")
    - delay = delay para a resposta
    
- Execute o arquivo RUN.bat
