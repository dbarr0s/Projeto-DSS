<p align="center">
  <img src="https://github.com/Duarte0903/DSS_UMinho/blob/main/EEUMLOGO.png"/>
</p>

<h1 align="center">Projeto da UC de Desenvolvimento de Sistemas de Software - 2023/2024</h1>
<h2 align="center">Sistemas de gestão de uma estação de serviço</h2>

# Introdução
Este sistema auxilia o funcionamento de uma estação da E.S.Ideal, uma cadeia de Estações de Serviço Auto que fornece aos seus clientes serviços de manutenção automóvel.

# Elementos deste repositório
Este repositório contém:
- Toda o código da implementação do sistema
- Scripts SQL para inicialização da base de dados bem como o seu povoamento
- Bibliotecas auxiliares à execução do sistema
- Modelos e diagramas de modelação do projeto
- Relatório final do projeto

# Funcionalidades
O sistema permite:
- Efetuar login de gerente e mecânico
- Inicialização e finalização de turnos
- Pedido de check-up geral
- Inicialização e finalização de check-ups
- Pedido de realização de serviço
- Inicialização e finalização de serviços
- Notificação do cliente acerca do estado do serviço pedido

# Observações importantes
- Este sistema funciona sobre uma camada de dados isto é, contém uma base de dados relacional onde estão guardados os dados da estação
- O sistema parte do princípio que eles existem, pelo que não dispõe de sistema de registo de clientes ou funcionários

# Execução do sistema
Cuidados especiais a ter na execução do sistema:
- É necessário inicializar e povoar a base de dados (todo o material necessário encontra-se [aqui](https://github.com/Pedrosilva03/dss-esideal/tree/38b93d30b833dc52f30bddba9f18abdcd19f6ea5/src/SQL))
- As credenciais de acesso à base de dados devem ser alterados [aqui](https://github.com/Pedrosilva03/dss-esideal/blob/38b93d30b833dc52f30bddba9f18abdcd19f6ea5/src/esideal/data/ConfigDAO.java) (campos ```USERNAME``` e ```PASSWORD```)

# Conclusão
Trabalho realizado por Pedro Silva, António Silva, Diogo Barros e Duarte Leitão.

Nota final: 12/20
