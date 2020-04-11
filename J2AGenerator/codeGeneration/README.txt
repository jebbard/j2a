Verwendung des Code-Generators:
-------------------------------

1.) Gesamtes Projekt "Tooling" aus dem SVN auschecken
2.) generation.properties anpassen (liegt neben dieser Datei)

	- "sourcePath" und "sourcePackage" definieren das Package, in dem die POs liegen,
	aus denen TOs generiert werden sollen.
	- "targetPath" und "targetBasePackage" sowie "targetChildPackage_*" definieren
	das Zielpackage, in dem die generierten TOs landen

	Infos zur Struktur der generierten TOs gibt es an folgender Stelle:
	/Fhunii_Docs/spec/technisch/TechnischeFestlegungen.ppt

	Alle Standard-Einstellungen sollten ansonsten funktionieren und sind
	halbwegs in der proeprties-Datei selbst dokumentiert; es sollte sichergestellt
	werden, dass alle in den Properties angegebenen relativen Pfade existieren.

	Bei Problemen auf Jens zugehen.

3.) Generator starten, dazu die Klasse

	/Tooling/src/de/fhunii/utils/toGenerator/generation/TOGenerator.java

	ausführen. Erstes Argument ist der absolute Pfad zur zu verwendenden
	properties-Datei. Es sollte am besten die folgende properties-Datei verwendet
	werden (wie in Schritt 3 bereits erwähnt):

	/Tooling/codeGeneration/generation.properties

4.) Läuft die Generierung fehlerfrei (siehe Console), dann landen im konfigurierten
    Zielpackage die generierten TOs.

    Am besten alle generierten Packages, die Klassen enthalten, markieren, und dann
    per Rechtslick > "Source Format" sowie > "Organize Imports" durchführen.

    Einige Fehler bleiben womöglich bestehen und müssen manuell korrigiert werden
    (z.B. zweideutige Klassennamen wie Date).

5.) Die generierten TOs dürfen natürlich manuell angepasst werden.

ACHTUNG: bei erneuter Generierung in das gleiche Target Package werden bestehende
Dateien mit gleichem Namen einfach überschrieben.