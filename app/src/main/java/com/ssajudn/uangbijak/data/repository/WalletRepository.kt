package com.ssajudn.uangbijak.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.ssajudn.uangbijak.data.model.Wallet
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class WalletRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun getWallets(userId: String): Flow<List<Wallet>> = callbackFlow {
        val query = firestore.collection("wallets")
            .whereEqualTo("userId", userId)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val wallets = snapshot.documents.mapNotNull { doc ->
                    val data = doc.toObject(Wallet::class.java)
                    data?.id = doc.id
                    data
                }
                trySend(wallets)
            }
        }

        awaitClose { listener.remove() }
    }
}