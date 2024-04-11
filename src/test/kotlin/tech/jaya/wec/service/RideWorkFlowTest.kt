package tech.jaya.wec.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import tech.jaya.wec.dao.DriverDao

@SpringBootTest(properties = ["spring.profiles.active=test"])
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RideWorkFlowTest {


    @Autowired
    private lateinit var driverDao: DriverDao

    @Test
    fun `when a customer requests a driver and there are drivers available`() {
    }

    @Test
    fun `when a customer requests a driver and there is no driver available`() {
    }
}