package de.adorsys.mobile.mecard

import it.auron.library.mecard.MeCard


object MecardGenerator {

     fun mapPersonToMeCard(person: de.adorsys.mobile.data.Person) = MeCard().apply {
        this.name = person.firstName
        this.surname = person.secondName
        // meCard.date = "1989-07-19"
        this.email = person.email
        this.addTelephone(person.phone)
        this.url = person.url
        this.address = person.adress
        this.org = person.company
    }.buildString()

}