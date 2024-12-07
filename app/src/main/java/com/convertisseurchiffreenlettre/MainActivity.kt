package com.convertisseurchiffreenlettre

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import android.app.AlertDialog // Pour AlertDialog
import android.widget.Toast // Pour afficher les Toasts
import android.text.TextWatcher
import android.text.Editable
import com.convertisseurchiffreenlettre.logics.EnglishConversion
import com.convertisseurchiffreenlettre.logics.FrenchConversion
import com.convertisseurchiffreenlettre.logics.GermanConversion
import com.convertisseurchiffreenlettre.logics.SpanishConversion
import com.convertisseurchiffreenlettre.shares.DialogBotYesNo


class MainActivity : AppCompatActivity() {

    private lateinit var inputNumber: EditText
    private lateinit var selectLanguage: Button
    private lateinit var resultText: TextView
    private var selectedLanguage: String = "fr" // Langue par défaut

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputNumber = findViewById(R.id.inputNumber)
        selectLanguage = findViewById(R.id.selectLanguage)
        resultText = findViewById(R.id.resultText)
        selectLanguage = findViewById(R.id.selectLanguage)

        val exitButton: Button = findViewById(R.id.exitButton)

        exitButton.setOnClickListener {
            val dialog = DialogBotYesNo(
                context = this,
                title = "Confirmation",
                message = "Voullez-vous quiter cette application?",
                positiveButtonAction = {
                    finish()
                },
                negativeButtonAction = {
                    Toast.makeText(this, "Veuillez saisir un autre nombre à convertir", Toast.LENGTH_SHORT).show()
                }
                )
            dialog.showDialog()
        }

        // Gestion de la sélection de langue
        selectLanguage.setOnClickListener {
            showLanguageSelectionDialog()
        }



        // Ajouter un TextWatcher pour convertir dès que l'utilisateur entre un chiffre
        inputNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Appeler la conversion à chaque changement de texte
                if (s != null && s.isNotEmpty()) {
                    convertAndDisplayResult()
                }else{
                    inputNumber.setText("0")
                    convertAndDisplayResult()
                    inputNumber.setText("")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Rien à faire ici
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Rien à faire ici
            }
        })
    }

    private fun showLanguageSelectionDialog() {
        // Liste des langues et leurs codes correspondants
        val languages = arrayOf("Français", "Anglais", "Espagnol", "Allemand")
        val languageCodes = arrayOf("fr", "en", "es", "de")

        // Inflate le layout personnalisé pour le dialogue
        val dialogView = layoutInflater.inflate(R.layout.dialog_select_language, null)

        // Création du constructeur de la boîte de dialogue
        val builder = AlertDialog.Builder(this).apply {
            setView(dialogView)
        }

        // Initialisation des vues dans le layout de dialogue
        val dialogTitle = dialogView.findViewById<TextView>(R.id.dialogTitle)
        val languageListView = dialogView.findViewById<ListView>(R.id.languageListView)

        // Adaptateur pour afficher la liste des langues
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, languages)
        languageListView.adapter = adapter

        // Création de la boîte de dialogue
        val dialog = builder.create()

        // Gestion des clics sur les éléments de la liste
        languageListView.setOnItemClickListener { _, _, which, _ ->
            // Mise à jour de la langue sélectionnée
            selectedLanguage = languageCodes[which]
            selectLanguage.text = languages[which] // Mise à jour du texte du bouton

            // Message de confirmation
            Toast.makeText(this, "Langue sélectionnée : ${languages[which]}", Toast.LENGTH_SHORT).show()

            // Appel de la méthode pour convertir et afficher le résultat
            convertAndDisplayResult()

            // Fermeture de la boîte de dialogue
            dialog.dismiss()
        }

        // Affichage de la boîte de dialogue
        dialog.show()
    }

    // Fonction qui effectue la conversion et affiche le résultat
    private fun convertAndDisplayResult() {
        val number = inputNumber.text.toString()
        if (number.isEmpty()) {
            resultText.text = "Zéro"
        } else {
            val result = convertNumberToWords(number.toLong(), selectedLanguage)
            when (selectedLanguage) {
                "fr" -> "FRANÇAIS"
                "en" -> "ENGLISH"
                "es" -> "ESPAÑOL"
                "de" -> "DEUTSCH"
                else -> "RESULTAT"
            }
            resultText.text = "$result"
        }
    }

    // Fonction pour convertir un chiffre en lettres
    private fun convertNumberToWords(number: Long, language: String): String {
        return when (language) {
            "fr" -> FrenchConversion(number).conversion()
            "en" -> EnglishConversion(number).converter()
            "es" -> SpanishConversion(number).conversion()
            "de" -> GermanConversion(number).conversion()
            else -> "Langue non supportée"
        }
    }


}