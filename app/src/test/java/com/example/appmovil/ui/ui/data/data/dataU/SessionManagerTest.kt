package com.example.appmovil.ui.ui.data.data.dataU

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.appmovil.data.SessionManager
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config     // <-- IMPORT IMPORTANTE

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)              // <-- AGREGA ESTA LÍNEA AQUÍ
class SessionManagerTest {

    private lateinit var context: Context
    private lateinit var session: SessionManager

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        session = SessionManager(context)
        session.logout()
    }

    @Test
    fun saveUser_guardaTodosLosDatosCorrectamente() {
        session.saveUser(
            name = "Juan Pérez",
            email = "test@mail.com",
            password = "1234",
            address = "Calle 123",
            phone = "987654"
        )

        assertEquals("Juan Pérez", session.getName())
        assertEquals("test@mail.com", session.getEmail())
        assertEquals("1234", session.getPassword())
        assertEquals("Calle 123", session.getAddress())
        assertEquals("987654", session.getPhone())
        assertTrue(session.isLoggedIn())
    }
}
