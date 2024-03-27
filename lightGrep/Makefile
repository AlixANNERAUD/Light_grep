SRC=src
TEST=test
CLASSES_DEST=build/classes
TESTS_DEST=build/test/classes
LIB=lib

PACKAGE_RACINE=fr/insarouen/iti/compilation/lightgrep
PACKAGE_AUTOMATE=$(PACKAGE_RACINE)/automate
PACKAGE_COMPILATEUR=$(PACKAGE_RACINE)/compilateur
PACKAGE_AST=$(PACKAGE_RACINE)/arbreSyntaxiqueAbstrait
PACKAGE_ANALYSEURS=$(PACKAGE_RACINE)/analyseurs
PACKAGE_EXP=$(PACKAGE_RACINE)/expressionRationnelle

all: javacc javac testsunitaires

javacc: $(SRC)/$(PACKAGE_ANALYSEURS)/AnalyseurSyntaxique.java

javac: analyseurs ast automate compilateur expressionRationnelle main

testsunitaires: $(TESTS_DEST)/TestExpressionRationnelle.class

main: $(CLASSES_DEST)/$(PACKAGE_RACINE)/Main.class

analyseurs: $(CLASSES_DEST)/$(PACKAGE_ANALYSEURS)/AnalyseurSyntaxique.class $(CLASSES_DEST)/$(PACKAGE_ANALYSEURS)/AnalyseurSyntaxiqueTokenManager.class $(CLASSES_DEST)/$(PACKAGE_ANALYSEURS)/ParseException.class $(CLASSES_DEST)/$(PACKAGE_ANALYSEURS)/SimpleCharStream.class $(CLASSES_DEST)/$(PACKAGE_ANALYSEURS)/Token.class $(CLASSES_DEST)/$(PACKAGE_ANALYSEURS)/TokenMgrError.class $(CLASSES_DEST)/$(PACKAGE_ANALYSEURS)/AnalyseurSyntaxiqueConstants.class

ast:  $(CLASSES_DEST)/$(PACKAGE_AST)/Concatenation.class $(CLASSES_DEST)/$(PACKAGE_AST)/ExpressionRationnelleElementaire.class  $(CLASSES_DEST)/$(PACKAGE_AST)/UnAN.class   $(CLASSES_DEST)/$(PACKAGE_AST)/ZeroAN.class $(CLASSES_DEST)/$(PACKAGE_AST)/ExpressionRationnelle.class  $(CLASSES_DEST)/$(PACKAGE_AST)/ExpressionRationnelleUnaire.class  $(CLASSES_DEST)/$(PACKAGE_AST)/Union.class  $(CLASSES_DEST)/$(PACKAGE_AST)/ZeroOuUn.class

automate: $(CLASSES_DEST)/$(PACKAGE_AUTOMATE)/Automate.class  $(CLASSES_DEST)/$(PACKAGE_AUTOMATE)/Etat.class  $(CLASSES_DEST)/$(PACKAGE_AUTOMATE)/Lettre.class  $(CLASSES_DEST)/$(PACKAGE_AUTOMATE)/LettreException.class  $(CLASSES_DEST)/$(PACKAGE_AUTOMATE)/OutilsPourAutomate.class  $(CLASSES_DEST)/$(PACKAGE_AUTOMATE)/Transitions.class

compilateur: $(CLASSES_DEST)/$(PACKAGE_COMPILATEUR)/AstEnAutomate.class

expressionRationnelle: $(CLASSES_DEST)/$(PACKAGE_EXP)/ExpressionRationnelle.class  $(CLASSES_DEST)/$(PACKAGE_EXP)/Intervalle.class

$(SRC)/$(PACKAGE_ANALYSEURS)/AnalyseurSyntaxique.java: $(SRC)/$(PACKAGE_ANALYSEURS)/AnalyseurSyntaxique.jj
	javacc -OUTPUT_DIRECTORY=$(SRC)/$(PACKAGE_ANALYSEURS)/ $<

$(CLASSES_DEST)/%.class: $(SRC)/%.java
	javac -d $(CLASSES_DEST) -cp $(CLASSES_DEST) -sourcepath src $<

$(TESTS_DEST)/%.class: $(TEST)/%.java
	javac -d $(TESTS_DEST) -cp $(CLASSES_DEST):$(LIB)/junit4.jar:$(LIB)/hamcrest-all.jar -sourcepath src $<

clean:
	rm -f $(SRC)/$(PACKAGE_ANALYSEURS)/*.java
	rm -rf $(CLASSES_DEST)/*
