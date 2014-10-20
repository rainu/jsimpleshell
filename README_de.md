jsimpleshell
============

## 1) Was ist JSimpleShell?

JSimpleShell (JSS) ist ein kleines Framework, mit dem man eine Shell-Umgebung implementieren kann. Diese Shell kann man ähnlich wie die Bash aus Unix verwendet: Man kann Kommandos aufrufen; durch den Verlauf iterieren; Variablen definieren; Schleifen ausführen; und vieles mehr... Und das Beste daran: Es ist Plattformunabhängig! Das heißt, dass man diese Shell unter Unix genauso gut steuern kann, wie unter Windows oder Mac OS.

Das ist die perfekte Lösung, wenn man keine große Lust und/oder Zeit hat, eine eigene GUI zu implementieren. Man kann ziemlich einfach ein Kommando definieren - eine Annotation genügt!

### 1.1) Lizens

Natürlich ist dieses Framework Open-Source! Es steht unter der [BSD License (3-Clause)](http://opensource.org/licenses/BSD-3-Clause) zur Verfügung.

## 2) Integration

### 2.1) Maven

JSS ist auf maven-central deployed. Das heißt, dass man sie ganz einfach in maven-projekten als dependency einbinden kann.

```xml
<dependency>
	<groupId>de.raysha.lib</groupId>
	<artifactId>jsimpleshell</artifactId>
	<version>3.0.1</version>
</dependency>
```

Die zum derzeitigen Zeitpunkt aktuelle Version kann man [hier](http://search.maven.org/#search|gav|1|g%3A%22de.raysha.lib%22%20AND%20a%3A%22jsimpleshell%22) finden.

### 2.2) Shell-Builder

Starten wir einmal mit einer leeren Shell. Das heißt, dass diese Shell nur die Standardkommandos benutzt. Um eine (Sub-)Shell zu erzeugen, wird der __ShellBuilder__ verwendet. Mit diesem lassen sich alle Einstellungen einer Shell vornehmen. 

```java
import java.io.IOException;

import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;

public class Example {

	public static void main(String[] args) throws IOException {
		Shell shell = ShellBuilder.shell("MyShell").build();
		shell.commandLoop();
	}
}
```

Nachdem eine Shell gebaut wurde, kann man diese "starten" indem man __commandLoop()__ aufruft. Diese Methode erfragt (solange wie möglich) Kommandos vom Benutzer und führt diese aus. Sobald der Benutzer diese Shell verlässt - z.b. durch eingabe von _exit_, wird diese Methode verlassen und die Anwendung hat wieder die Kontrolle.

Der ShellBuilder ist in logische "Abschnitte" unterteilt, um die Konfiguration einer Shell zu kategoriesieren. In den folgenden Kapitteln wird auf die jeweiligen "Abschnitte" eingegangen.

#### 2.2.1) Verhalten (behavior)

Im Verhaltens-Abschnitt des ShellBuilders kann man das Verhalten der Shell konfigurieren. Um genau zu sein, kann man in diesem Abschnitt folgendes tun:

| Beschreibung | Methode | Standardeinstellung |
|--------------|---------|---------------------|
|(AUX-)Handler hinzufügen (siehe [Kapittel Command(-Handler)](#23-handler)) | <ul><li>addHandler()</li><li>addAuxHandler()</li></ul>| keine (AUX-)Handler|
|Soll auf &lt;STRG&gt;-C des Benutzers reagieret werden? Wenn nicht, führt das zum Beenden der Anwendung!|<ul><li>setHandleUserInterrupt()</li></ul>| __false__ - &lt;STRG&gt;-C beendet die Anwendung|
|Sollen die Datei- oder Verzeichnis-Pfade automatisch vervollständigt werden? (siehe [Kapittel Auto-Vervollständigung](#33-auto-vervollst%C3%A4ndigung))|<ul><li>disableFileNameCompleter()</li><li>enableFileNameCompleter()</li><li>setFileNameCompleter()</li></ul>|__true__ - die Auto-Vervollständigung ist aktiviert|
|Soll das __exit__-Kommando verwendet werden?|<ul><li>enableExitCommand()</li><li>disableExitCommand()</li><li>setExitCommand()</li></ul>|__true__ - das exit Kommando wird verwendet. Sollte kein exit verwendet werden, hat der Benutzer keine Chance die Shell zu verlassen, es sei denn es existiert ein anderes Kommando, welches eine ExitException werfen kann.|
|Definieren, wo sich die Verlaufs-Datei der Shell befindet.|<ul><li>setHistoryFile()</li></ul>| __null__ - es wird kein Verlauf nach dem Beenden der Anwendung gespeichert.|
|Definieren, wo sich das Makro-Verzeichnis der Shell befindet. (siehe [Kapittel Makros](#38-makros))|<ul><li>setMacroHome()</li></ul>|__null__ - es wird das aktuelle Arbeitsverzeichnis verwendet.|
|De-/Aktivieren der Prozess-Kommandos. Diese Kommandos können aus der Shell heraus externe Prozesse starten. (siehe [Kapittel Prozess-Starter](#37-prozess-starter))|<ul><li>enableProcessStarterCommands()</li><li>disableProcessStarterCommands()</li><li>setProcessStarterCommands()</li></ul>|__false__ - die Prozess-Kommandos sind aus Sicherheitsgründen nicht aktiviert.|
|De-/Aktivieren ob die Kommandos mit einem Prefix automatisch vervollständigt werden sollen, wenn noch kein Anfangszeichen eines Kommandos eingegeben wurde.|<ul><li>disableAutocompleOfSpecialCommands()</li><li>enableAutocompleOfSpecialCommands()</li><li>setAutocompleOfSpecialCommands()</li></ul>|__false__ - die Auto-Vervollständigung wird erst dann aktiv, nachdem der Benutzer den Prefix des Kommandos eingegeben hat.|

#### 2.2.2) Aussehen (look)

Im Aussehens-Abschnitt wird das Aussehen der Shell definiert. Dazu zählen konkret die folgenden Dinge:

| Beschreibung | Methode | Standardeinstellung |
|--------------|---------|---------------------|
| Der Anwendungsname. Dieser wird beim eintritt der Shell angezeigt. | <ul><li>setAppName()</li></ul> | __null__ - der Anwendungsname ist nicht vergeben und wird somit auch nicht angezeigt. |
| Der Prompt-Teil der Shell. Der gesamt Prompt setzt sich aus allen Eltern- und der aktuellen Shell zusammen. | <ul><li>setPrompt(PromptElement)</li><li>setPrompt(String)</li></ul> | Dies ist ein Pflichtparameter und muss schon beim Initialisieren eines ShellBuilders mit angegeben werden. Das besondere an einem __PromptElement__ ist, dass dies bei jedem Rendern aufgerufen wird. Somit kann man eine klasse definieren, die den Prompt dynamisiert. |
| Die Farbausgabe kann konfiguriert werden. | <ul><li>enableColor()</li><li>disableColor()</li><li>setColor()</li></ul> | __true__ - es ist möglich, Farbausgaben zu erzeugen. Sollte die Farbeausgabe deaktiviert sein, kann auch troz Verwendung der entsprechenden Farbausgabe-Methoden keine Ausgabe in Farbe erzeugt werden! Somit kann die Anwendung mit Farbausgaben arbeiten, aber sie evtl. je nach System ein- bzw. ausschalten. |

#### 2.2.3) Ein-/Ausgabe (io)

Eine Shell muss nicht zwangsläufig die Standard Ein-/Aus-/Fehl- Ausgabe verwenden! Die E/A-Streams können benutzerspezifisch definiert werden. Dies stellt man in dem Ein-/Ausgabe-Abschnitt ein:

| Beschreibung | Methode | Standardeinstellung |
|--------------|---------|---------------------|
| Definieren, dass die Shell eine eigene [Console](https://github.com/jline/jline2) verwenden soll. | <ul><li>setConsole(Console)</li></ul> | Es wird eine eigene Console verwendet. Es wird __nicht__ empfohlen eine eigene Console zu verwenden! |
| Die Input- Output-Streams können definiert werden. | <ul><li>setConsole(InputStream, OuputStream)</li></ul> | __System.in__, __System.out.__ - es wird die standard Ein-/Ausgabe verwenden. |
| Die Fehlerausgabe kann definiert werden.| <ul><li>setError</li></ul> | __System.err__ - es wird die standard Fehlerausgabe verwendet. |

___Achtung___: Die Kommandos der Anwendung sollten nicht _System.out_, _System.err_, _System.in_ direkt verwenden! Da die Kommandos die Ein-/Ausgabe der __Shell__ verwenden sollten, muss der [Input- bzw. Output-Builder](#25-input-and-output) verwendet werden. Sollte die Shell die standard Ein-/Ausgabe verwenden wird dies auf den ersten Blick den gleichen Effekt haben. Werden von der Shell jedoch einmal andere Streams verwendet, muss die Anwendung evtl. mühsam umgeschrieben werden.

### 2.3) Handler

Ein Handler kann im Grunde _alles_ sein! Es ist dem Entwickler überlassen, wie fein er die Trennung der Zuständigkeiten definiert. Beinhaltet ein Handler __@Command__-Annotations werden diese Kommandos entsprechend in der "Kommando-Tabelle" der Shell aufgenommen. Diese kann aber gleichzeitig (je nach implementierten Interface) ein [Access-Manager](#232-access-manager), [Converter](#236-converter) und/oder noch mehr sein. In einer Shell kann man zwei Arten von Handlern registrieren:
* __MainHandler__
* __AUX-Handler__

Sie unterscheiden sich nur in der Gültigkeit in einer Shell. Der __MainHandler__ ist nur innerhalb __einer__ Shell gültig. Ein AUX-Handler hingegen ist in der definierten Shell __und allen SubShell__, die diese Shell als Elternteil besitzt, gültig. In allen anderen Aspekten sind diese beiden Handlerarten gleichwertig.

In den folgenden Abschnitten wird jeder Typ eines Handlers erläutert. 

#### 2.3.1) Command-Handler

Ein Command-Handler ist ein Handler, der mindestens ein Kommando beinhaltet. Das heißt eine Methode, die mit der __@Command__-Annotation annotiert wurde. Jede Methode mit einer solchen Annotation wird als Kommando in der "Kommando-Tabelle" der Shell eingetragen. Es ist nach Möglichkeit darauf zu achten, dass kein Kommando doppelt in der Kommando-Tabelle vorhanden ist. Denn sonst kann der Benutzer __keines__ dieser Kommandos ausführen. Ein Kommando gilt dann als Duplikat wenn:
* es den gleichen __Namen__ hat
* und die gleiche __Parameteranzahl__
* mit der gleichen __Parametertypen__

Das folgende Code-Beispiel dient zur Demonstration und beinhaltet solche Duplikate:
```java
...

@Command(name="command")
public void commandOne(String param){
	//do something 
}

@Command
public void command(String param){
	//do something
}

...
```

Sollte in der @Command-Annotation nicht explizit der Name des Kommandos definiert sein, so wird der __Methodenname__ als Kommandoname verwendet. Da die Methode __commandOne__ in ihrer Annotation den Namen __command__ verwendet und die __Methode command__ in ihrer Annotation __keinen__ Namen definiert, stellen diese beiden Methoden Kommando-Duplikate dar!

##### 2.3.1.1) Command-Annotation

Die @Command-Annotation _kann_ Parameter übergeben bekommen. Diese Parameter und deren Bedeutung sind in der folgenden Tabelle aufgelistet:

|Name|Beschreibung|Standardwert|
|----|------------|------------|
|name|Der Name des Kommandos.|__null__ - es wird der Methodenname verwendet. Sollte der Methodenname in "Höckerschreibweise" geschrieben wurden sein, wird zwichen jedem Großbuchstaben ein Bindestrich eingefügt und der Großbuchstabe in einen Kleinbuchstaben verwandelt. Zum Beispiel wird aus dem Methodennamen __setSecretPassword__ der Kommandoname __set-secret-password__.|
|description|Die (ausführliche) Beschreibung des Kommandos. Diese Beschreibung wird bei der Hilfe des Kommandos ausgegeben.|__null__ - es gibt keine Beschreibung.|
|abbrev|Die Abkürzung des Kommandos. Diese kann der Benutzer alternativ zu dem kompletten Kommandonamen eingeben. Auch die Abkürzungen sollten unter den Kommandos einer Shell eindeutig sein.|__null__ - der Anfangsbuchstabe des Methodennamens. Sollte der Methodenname in "Höckerschreibweise" geschrieben wurden sein, wird der Anfangsbuchste und jeder folgende Großbuchstabe für die Abkürzung verwendet. Zum Beispiel wird aus dem Methodennamen __setSecretPassword__ die Abkürzung __ssp__.|
|header|Die Kopf(-zeile) eines Kommandos. Diese wird bei _jedem_ Aufruf des Kommandos ausgegeben.|__null__ - es gibt keine Kopfzeile.|
|displayResult|Soll der Rückgabewert des Kommandos (und damit der Java-Methode) ausgegeben werden? Sollte der Rückgabewert kein String sein, so wird versucht das Ergebnis in ein String zu [konvertieren](#236-converter).|__true__ - der Rückgabewert des Kommandos wird ausgegeben.|
|startsSubshell|Gibt an, ob dieses Kommando eine SubShell öffnen (kann). Diese Information wird von JSS ___benötigt___, um dieses Komamndo bei einer evtl. [Schleife](#36-schleifen) des Benutzers auszuführen. Die Shell befindet sich dann im ["RecordMode"](#24-Aufnahmemodus). |__false__ - das Kommando wird keine SubShell starten. Dies muss __unbedingt__ angegeben werden, sollte dies doch der Fall sein! |

Man kann __name__, __description__, __abbrev__, und __header__ einen festen Wert geben, der bei jeder verwendeten Sprache (Deutsch, Englisch, ...) verwendet wird. Man kann in diesen Parametern aber auch (eindeutige) Schlüssel vergeben, die dann anschließend von den verwendeten [Message-Resolver](#234-message-resolver) aufgelöst werden. __Alle__ Standardkommandos von JSS haben eindeutige Schlüssel, sodass man sie auch ggf. umbennenen kann. Wie dies im Detail funktioniert siehe [Kapittel Message-Resolver](#234-message-resolver).

##### 2.3.1.2) Param-Annotation

Jeder Parameter eines Kommandos _kann_ mit einer __@Param__-Annotation annotiert werden. Das ist immer sinnvoll, da JSS die Parameternamen zur Laufzeit __nicht__ ermitteln kann! Ist keine Annotation vorhanden, wäre das Verhalten so, als ob eine Annotation mit den Standardwerten verwendet wurden wäre. Es gibt noch weitere Einstellmöglichkeiten, die in der folgenden Tabelle aufgelistet werden:

|Name|Beschreibung|Standardwert|
|----|------------|------------|
|value|Der (Anzeige-)Name des Parameters.|__null__ - der Parametername setzt sich zusammen aus __p__ gefolgt von dem __Parameterindex__. Demzufolge würde der erste Parameter __p0__, der zweite __p1__ usw. lauten.|
|description|Die Beschreibung des Parameters. Diese wird in der Hilfe angezeigt.|__null__ - es gibt keine Beschreibung.|
|type|Sollte der Java-Typ _Object_ oder _String_ sein, kann mit dieser Einstellung präzesiert werden, welchen Typ der Parameter besitzen soll. Dieser Typ wird u.a. von den [Candidates-Chooser](#235-candidates-chooser) verwendet, um den Benutzer bei der Eingabe zu unterstützen. |__null__ - es wird der Java-Typ verwendet um die [Candidates-Chooser](#235-candidates-chooser) zu bedienen.|

Auch bei den Parametern sollten möglichst keine (logischen) Duplikate vorkommen. In erster Line wird zwar auf die Eingabereihenfolge geachtet. Aber der Benutzer kann diese bei Bedarf auch ändern, indem er den Namen des Parameters verwendet. Dieser muss in diesem Falle dann eindeutig sein! Auch kann man (wie bei der Command-Annotation) feste Werte für __value__ und __description__ verwenden. Alternativ können auch hier (eindeutige) Schlüssel verwendet werden, die anschließend der verwendete [Message-Resolver](#234-message-resolver) auflöst. __Alle__ Parameter der Standard-Kommandos verwenden Schlüssel, sodass man sie ggf. umbennenen kann.

#### 2.3.2) Access-Manager

#### 2.3.3) Shell-Hooks

#### 2.3.4) Message-Resolver

#### 2.3.5) Candidates-Chooser

#### 2.3.6) Converter

### 2.4) Dependency injection

### 2.5) Input and Output

### 2.4) Aufnahmemodus

## 3) Benutzung

### 3.1) Help

### 3.2) Parameter

### 3.3) Auto-Vervollständigung

### 3.4) Sub-Shells

### 3.5) Variablen

### 3.6) Schleifen

### 3.7) Prozess-Starter

### 3.8) Makros

### 3.9) Sprache
