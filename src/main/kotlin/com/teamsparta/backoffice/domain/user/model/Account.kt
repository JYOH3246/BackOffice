package com.teamsparta.backoffice.domain.user.model

import com.teamsparta.backoffice.domain.user.dto.AccountRequest
import com.teamsparta.backoffice.domain.user.dto.AccountResponse
import jakarta.persistence.*

@Entity
@Table(name = "account")
class Account(

        @Column(name = "balance")
        var balance: Int = 0,

        ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    fun modifyAccount(request: AccountRequest) {
        balance = request.balance

    }

}

fun Account.toResponse(): AccountResponse {
    return AccountResponse(
            balance = balance
    )
}