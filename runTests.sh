#STYLE=textui
STYLE=swingui
#STYLE=awtui
java -cp dist/javagnupg-0.1.2-alpha.jar:lib/junit-3.8.1.jar junit.${STYLE}.TestRunner com.freiheit.gnupg.GnuPGTestSuite
