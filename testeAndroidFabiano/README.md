# Android - Fabiano Gomes #

## Conceito:
### Trata-se de um aplicativo android nativo, utilizando kotlin como linguagem de desenvolvimento, formulado com o objetivo de comunicar-se com a API Olho Vivo (http://www.sptrans.com.br/desenvolvedores/api-do-olho-vivo-guia-de-referencia/).

## Introdução:
###  Esse aplicativo é capaz de comunicar-se com a API, de modo a receber informações como a posições de veículos, pontos de paradas e corredores presentes na cidade de São Paulo. Sendo assim, você pode usar o nome de uma linha de ônibus ou a rua de uma parada para conseguir o código que as representa e a partir disso obter informações como previsão da chegada de uma determinada linha em um ponto, ver a posição dos ônibus e das paradas no mapa do aplicativo, além de outras informações relevantes.

## Funcionalidades:

### Linhas: É possível recuperar informações de uma linha como os veículos que fazem parte e suas paradas.
### Paradas: Exibição de informações de uma parada como posição, nome e linhas que passam por ela.
### Filtragem: É possível pesquisar por informações específicas escolhendo um tipo de operação. Deste modo, o usuário decide o que buscar e o resultado será exibido para ele assim que recuperado.
### Previsão de chegada: Informando o código de uma parada você haverá a listagem de todas as linhas que passam por aquela parada e a previsão dos próximos veículos a chegarem ali.
### Posição: É exibido no mapa a posição das paradas de uma linha específica de ônibus, de acordo com a pesquisa do usuário, além de mostrar a posição atualizada dos ônibus que compõem essa linha. Ao clicar na parada, informações como o nome dessa parada são exibidas, já os ônibus mostram sua linha e se eles são acessíveis a deficientes.
### Refresh automático: A cada 60 segundos o mapa atualiza
### Rotas: Ao clicar e manter o dedo na tela em um ponto do mapa, o aplicativo abrirá o google maps exibindo uma rota até o ponto desejado.
### Trânsito: As ruas do mapa são coloridas em verde, amarelo e vermelho, representando a velocidade das vias e o nível de congestionamento das mesmas.
### Recuperação de posição: O usuário pode clicar no ícone no canto superior direito do mapa e o mapa centralizará em sua posição.
### Corredores: Pesquisando pelos corredores, o usuário receberá informações relativas a posição de todos os corredores de São Paulo. Caso a pessoa deseje saber as paradas de um corredor específico, basta buscar por seu código e tais posições serão exibidas no mapa.
###  Testes: A aplicação possui uma série de testes relacionadas as operações básicas de comunicação com a API, haja vista que a comunicação foi bastante problemática, devido a um problema com a geração de keys.

## Decisões tomadas:
### Ao longo do desenvolvimento, foi necessário tomar algumas decisões pouco hortodoxas a respeito de funcionalidades do sistema. Focando em oferecer a melhor UX para os usuários, decidir por trabalhar utilizando dois fragments, sendo que o primeiro, de busca, conta com um recyclerview para exibir a lista de retorno de algumas buscas mais simples, enquanto o segundo fragment, de mapa, é responsável por exibir os resultados que necessitam do mapa como as posições de parada, paradas de corredor e veículos.

### Apenas uma biblioteca externa foi utilizada no desenvolvimento, sendo esta o retrofit. É responsável por lidar com todas as requisições http feitas pelo aplicativo. Desse modo, é a entidade responsável por fazer a comunicação do app com a API. Inicialmente, foi considerado a possibilidade de utiliza-la em conjunto com Room para construir uma espécie de cache viabilizando a utilização do aplicativo em modo offline, entretanto, essa funcionalidade perde um pouco do sentido após a implementação do auto refresh para o mapa, além de que, seria preciso reescrever os dados gravados em banco a cada requisição, o que poderia acabar exigindo um esforço de processamento e memória com pouco retorno.

### Quanto a padrões de código e arquitetura, o aplicativo utiliza o MVVM, sendo a arquitetura recomendada pelo Google para o desenvolvimento de aplicações devido a facilidade em criar códigos escaláveis. Além disso, alguns príncipios como SOLID e KISS para a elaboração de classes e requisições visando boa readability, além de facilitação na manutenção. Singleton foi aplicado para o retrofit, evitando múltiplas instâncias do mesmo. Além disso, a aplicação foi construída usando Single Activity em conjunto com Navigation UI.

### Em respeito do recycler view no fragment de pesquisa, alguns problemas foram enfrentados. O primeiro dele foi o desaparecimento dos dados quando eram numerosos e o teclado era aberto. Esse erro foi corrigido. Entretanto, dentre algumas coisas que eu gostaria de fazer, porém não foram possíveis, temos o scroll de acordo com o recebimento de informações. Entretando, este não funcionava e, invés de focar no útlimo item recebido, ficava faltando alguns itens, então optei por não implementar e exibir um toast para o usuário informando que mais dados foram recebidos, para que ele mesmo pudesse rolar o recycler. Eu também estava fechando o teclado após a busca, porém essa ação gerava alguns bugs ocasionais em relação ao recyclerview, então optei pelo mal menor e removi essa ação, embora o código permaneça comentado no arquivo.

# Vídeo pode ser acessado aqui: https://youtu.be/oOIP4jRRKkA
