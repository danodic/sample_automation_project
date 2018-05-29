# Prova Sicredi
Autor: Danilo Guimaraes

### Notas:
* Projeto criado em Java utilizando Maven para controle de build e dependências.
* Dependências:
  * TestNG - Gerenciamento e execução de testes.
  * Selenium 3.0.11 - Automação do browser.
  * Extent Reports 3 - Geração de relatórios.
  * SnakeYAML - Input de dados utilizando arquivos YAML.
* Sobre os testes:
  * Os testes podem ser encontrados em **com.danilo.prova_sicredi.tests.GroceryCrudTest**
    * São dois métodos de teste mais um método de DataProvider.
    * Arquivo de dados está em **data/grocery_crud/001_add_customer.yaml**
* Sobre o projeto:
  * Foi utilizado o padrão de PageObjects, classes de PageObject podem ser encontradas no pacote **com.danilo.prova_sicredi.pageobjects**
  * O pacote **com.danilo.prova_sicredi.support** contém um micro-framework que toma conta dos seguintes pontos:
    * Relatório em HTML utilizando Extent Reports:
      * Reporting automatizado utilizando listeners de TestNG e Selenium (WebDriverEventListener)
      * Captura e adição automatizada de excessões no relatório.
    * Um contexto de teste que gerencia o driver do Selenium e o relatório:
      * Gerenciamento e criação dos contextos é feito pela classe **com.danilo.prova_sicredi.support.factories.ContextFactory** através do ID das threads.
      * Classe de contexto possibilita a execução de testes em paralelo.
    * Classe **com.danilo.prova_sicredi.support.PageObject** para agilizar o uso de PageObjects.
    * Classe **com.danilo.prova_sicredi.support.Settings** para gerenciar configurações:
      * Configurações do projeto podem ser encontradas no arquivo config.properties.
      * A classe settings faz o parse das configurações no config.properties.
      * Esta class também faz o override de configurações através de linha de comando, utilizando argumentos de VM (ex.: -Dframework.browser="chrome")
  * O diretório report tem um relatório de exemplo, que será sobrescrito por novas execuções.
  * Classes do pacote support devem estar todas documentadas com comentários e javadoc.
  * Desenvolvido utilizando Eclipse Oxygen e testado com Maven 3.5.3.
    * Vídeo da execução de teste: https://www.youtube.com/watch?v=6zA5PtoYX9w
