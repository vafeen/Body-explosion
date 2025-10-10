package ru.vafeen.presentation.root

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.vafeen.domain.local_database.TestLocalRepository
import javax.inject.Inject

@HiltViewModel
internal class NavRootViewModel @Inject constructor(
    private val testLocalRepository: TestLocalRepository
) : ViewModel() {

}