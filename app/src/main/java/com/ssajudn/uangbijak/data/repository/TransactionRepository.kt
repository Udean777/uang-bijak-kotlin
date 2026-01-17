package com.ssajudn.uangbijak.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ssajudn.uangbijak.data.model.Transaction
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun getTransactions(userId: String): Flow<List<Transaction>> = callbackFlow {
        val query = firestore.collection("transactions")
            .whereEqualTo("userId", userId)
            .orderBy("date", Query.Direction.DESCENDING)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val transactions = snapshot.documents.mapNotNull { doc ->
                    val data = doc.toObject(Transaction::class.java)
                    data?.id = doc.id
                    data
                }
                trySend(transactions)
            }
        }

        awaitClose { listener.remove() }
    }
}