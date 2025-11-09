# Loja – Cálculo de Frete (Java 17 + Maven + JUnit 5)

Projeto exemplo com serviço de frete baseado em **peso**, **UF** e **promoções** (frete grátis por cupom ou subtotal).
- Arquitetura: **Ports & Adapters**
- Testes: **JUnit 5 + Mockito**
- Cobertura: **JaCoCo**

## Importar no Eclipse
File → Import → Maven → Existing Maven Projects → selecione esta pasta.

## Rodar
```
mvn test
mvn jacoco:report  # abre target/site/jacoco/index.html
```
