package de.adorsys.mobile.data

import de.adorsys.mobile.storage.FirebaseProvider
import java.util.*

object PersonRepository {

    var person: Person

    init {
        person = writePerson()
    }

    private fun writePerson(): Person {
        return Person(
            UUID.randomUUID().toString(),
            "Isabella",
            "Caspari",
            "+49 9489 29898",
            "Fürther Str.246a, 90429 Nürnberg",
            "adorsys GmbH & Co KG",
            "ica@adorsys.de",
            "https://github.com/IsabellaCaspari"
        )
    }

    fun writePerson(person: Person) {
        FirebaseProvider.writePerson(
            person
        )
    }

    fun updatePerson(person: Person) {
        FirebaseProvider.updatePerson(
            person
        )
    }
}