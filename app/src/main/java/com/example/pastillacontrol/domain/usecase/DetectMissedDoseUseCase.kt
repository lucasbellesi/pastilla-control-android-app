package com.example.pastillacontrol.domain.usecase

class DetectMissedDoseUseCase {
    fun isMissed(nowEpochMillis: Long, graceDeadlineAtEpochMillis: Long, isTaken: Boolean): Boolean {
        if (isTaken) return false
        return nowEpochMillis > graceDeadlineAtEpochMillis
    }
}
