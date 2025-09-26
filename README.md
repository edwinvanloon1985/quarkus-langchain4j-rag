# quarkus-langchain4j-rag

## Quick start
* Vereist JDK 21+
* Vereist Maven 3+
* Vereist Docker of Podman

Applicatie bouwen:
```shell
  mvn clean install
```

Applicatie starten:
```shell
  mvn quarkus:dev
```
De applicatie kan vervolgens gevonden worden op [http://localhost:8080](http://localhost:8080).

## Doelstellingen
Dit is een POC _(proof of concept)_ project met als doelstelling:
* Een demonstratie dat een berg aan organisatie documentatie* middels een website op een makkelijke manier ontsloten kan worden
  * _(Wij maken voor de demonstratie alleen gebruik van geprepareerde dummy documenten)_ 
* Over een bepaald onderwerp moet het héél eenvoudig zijn de relevante passages te vinden 
  * Hier een samenvatting van te krijgen, evenals alle gewenste details 
  * Hierbij moeten ook gerelateerde passages in andere documenten genoemd of naar verwezen worden
* Verder is het onderzoeken wat hier nog meer mee mogelijk is en welke wensen er nog meer zijn

## Technologie
Dit project is origineel gebaseerd op de [Quarkus LangChain4j Workshop](https://quarkus.io/quarkus-workshop-langchain4j/) aangeboden door Red Hat en vrij beschikbaar op [GitHub](https://github.com/quarkusio/quarkus-workshop-langchain4j)

Het `quarkus-langchain4j` framework dient als basis voor integratie met een LLM: 
  * `LangChain4j` stelt ons op eenvoudige wijze in staat de LLM op verschillende manieren in te zetten
  * `Quarkus` sluit aan op de kennis en stack die wij in onze organisatie reeds gebruiken 
  * `RAG` _(Retrieval Augmented Generation)_ zetten wij in om onze documentatie middels een LLM eenvoudige beschikbaar en doorzoekbaar te maken 

Als LLM-aanbieder is voor `Mistral` gekozen, want:
  * Mistral is een Franse organisatie met een meer academische invalshoek dan andere partijen 
  * Mistral biedt [open modellen](https://docs.mistral.ai/getting-started/models/models_overview/#open-models) aan voor eigen gebruik
  * Mistral modellen kenmerken zich door efficiëntie, weinig extra poespas of gevlei 

Als LLM is voor het model `Mistral Small 3.2` gekozen, want:
  * Dit is een algemeen open model van juni 2025
  * Dit is verfijning van [Mistral Small 3.1](https://mistral.ai/news/mistral-small-3-1)
  * Dit model ondersteunt meerdere talen, waaronder Nederlands
