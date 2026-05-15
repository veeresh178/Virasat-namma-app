package com.virasatnamma.data.repository

import com.virasatnamma.data.local.CheckInDao
import com.virasatnamma.data.local.CheckInEntity
import com.virasatnamma.data.local.DigitalPassport
import com.virasatnamma.data.local.HeritageLocation
import com.virasatnamma.data.local.SiteDao
import com.virasatnamma.data.local.SiteEntity
import com.virasatnamma.data.local.VisitRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository for Heritage Sites
 * Implements MVVM pattern for data management
 */
class HeritageRepository(
    private val siteDao: SiteDao,
    private val checkInDao: CheckInDao
) {
    
    fun getAllSites(): Flow<List<HeritageLocation>> {
        return siteDao.getAllSites().map { sites ->
            sites.map { it.toHeritageLocation() }
        }
    }
    
    suspend fun getSiteById(id: String): HeritageLocation? {
        return siteDao.getSiteById(id)?.toHeritageLocation()
    }
    
    suspend fun searchSites(query: String): List<HeritageLocation> {
        return siteDao.searchSites("%$query%").map { it.toHeritageLocation() }
    }
    
    fun getSitesByCategory(category: String): Flow<List<HeritageLocation>> {
        return siteDao.getSitesByCategory(category).map { sites ->
            sites.map { it.toHeritageLocation() }
        }
    }
    
    suspend fun initializeSampleData() {
        val siteCount = siteDao.getSiteCount()
        if (siteCount == 0) {
            siteDao.insertSites(generateSampleSites())
        }
    }
    
    suspend fun addCheckIn(siteId: String, siteName: String, siteImageUrl: String = "") {
        val checkIn = CheckInEntity(
            siteId = siteId,
            siteName = siteName,
            siteImageUrl = siteImageUrl,
            synced = false
        )
        checkInDao.insertCheckIn(checkIn)
    }
    
    fun getAllCheckIns(): Flow<List<VisitRecord>> {
        return checkInDao.getAllCheckIns().map { checkIns ->
            checkIns.map { it.toVisitRecord() }
        }
    }
    
    suspend fun isVisited(siteId: String): Boolean {
        return checkInDao.getLatestCheckInForSite(siteId) != null
    }
    
    suspend fun getVisitedSiteIds(): List<String> {
        return checkInDao.getVisitedSiteIds()
    }
    
    suspend fun getDigitalPassport(): DigitalPassport {
        val totalSites = siteDao.getSiteCount()
        val visitedCount = checkInDao.getVisitedSitesCount()
        
        return DigitalPassport(
            totalSites = totalSites,
            visitedSites = visitedCount,
            visitPercentage = if (totalSites > 0) (visitedCount * 100f) / totalSites else 0f,
            visits = emptyList()
        )
    }
    
    suspend fun getSiteCount(): Int {
        return siteDao.getSiteCount()
    }
    
    suspend fun updateSiteVisitCount(siteId: String) {
        val site = siteDao.getSiteById(siteId)
        site?.let {
            siteDao.updateSite(it.copy(visitCount = it.visitCount + 1))
        }
    }
    
    suspend fun getUnsyncedCheckIns(): List<CheckInEntity> {
        return checkInDao.getUnsyncedCheckIns()
    }
    
    suspend fun markCheckInSynced(checkIn: CheckInEntity) {
        checkInDao.updateCheckIn(checkIn.copy(synced = true))
    }
    
    private fun SiteEntity.toHeritageLocation(): HeritageLocation {
        return HeritageLocation(
            id = id,
            name = name,
            descriptionEn = descriptionEn,
            descriptionKn = descriptionKn,
            shortDescription = shortDescription,
            imageUrl = imageUrl,
            audioUrl = audioUrl,
            latitude = latitude,
            longitude = longitude,
            hiddenFacts = hiddenFacts.split("|").filter { it.isNotEmpty() },
            category = category,
            yearEstablished = yearEstablished,
            rating = rating,
            isVisited = false
        )
    }
    
    private fun CheckInEntity.toVisitRecord(): VisitRecord {
        return VisitRecord(
            id = id,
            siteId = siteId,
            siteName = siteName,
            siteImageUrl = siteImageUrl,
            timestamp = timestamp
        )
    }
    
    private fun generateSampleSites(): List<SiteEntity> {
        return listOf(
            SiteEntity(
                id = "site_001",
                name = "Virupaksha Temple",
                descriptionEn = "Dedicated to Lord Shiva, this ancient temple remains the spiritual center of Hampi. Its towering gopuram, intricate stone carvings, and lively evening aarti create a living heritage experience for pilgrims and travelers.",
                descriptionKn = "ಶಿವನಿಗೆ ಸಮರ್ಪಿತವಾದ ಇದು ಭಾರತದ ಅತ್ಯಂತ ಹಳೆಯ ಕಾರ್ಯನಿರ್ವಹಿಸುವ ದೇವಾಲಯವಾಗಿದೆ.",
                shortDescription = "7th century Shiva temple, the soul of Hampi",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e0/Hampi_Virupaksha_Temple_2014.jpg/1200px-Hampi_Virupaksha_Temple_2014.jpg",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                latitude = 15.3352,
                longitude = 76.4620,
                hiddenFacts = "Oldest functioning temple in India|Inverted shadow of the gopuram falls on a wall|Survived the destruction of Hampi",
                category = "Temple",
                yearEstablished = 740
            ),
            SiteEntity(
                id = "site_002",
                name = "Vittala Temple",
                descriptionEn = "World-famous for its stone chariot and musical pillars, Vittala Temple is a masterclass in Vijayanagara architecture. Its sculpted halls, carved corridors, and resonant pillars make it one of Hampi’s most unforgettable monuments.",
                descriptionKn = "ಪ್ರಸಿದ್ಧ ಕಲ್ಲಿನ ರಥ ಮತ್ತು ಸಂಗೀತ ಹೊರಹೊಮ್ಮಿಸುವ ಸ್ತಂಭಗಳಿಗೆ ಹೆಸರಾಗಿದೆ.",
                shortDescription = "Iconic stone chariot and musical pillars",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e4/The_Stone_Chariot_of_Hampi.jpg/1200px-The_Stone_Chariot_of_Hampi.jpg",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                latitude = 15.3389,
                longitude = 76.4733,
                hiddenFacts = "Wheels of the chariot could once rotate|Pillars produce 7 musical notes|Actually a shrine for Lord Vishnu",
                category = "Temple",
                yearEstablished = 1500
            ),
            SiteEntity(
                id = "site_003",
                name = "Mysore Palace",
                descriptionEn = "A grand Indo-Saracenic royal residence, Mysore Palace is the historic seat of the Wadiyar dynasty. Its ornate halls, colorful frescoes, and spectacular Dasara illumination bring royal Karnataka heritage to life.",
                descriptionKn = "ಒಡೆಯರ್ ರಾಜವಂಶದ ಭವ್ಯ ಇಂಡೋ-ಸಾರ್ಸೆನಿಕ್ ಅರಮನೆ.",
                shortDescription = "Grand Indo-Saracenic royal residence",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/06/Mysore_Palace_Morning_View.jpg/1200px-Mysore_Palace_Morning_View.jpg",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                latitude = 12.3052,
                longitude = 76.6552,
                hiddenFacts = "Illuminated by 97,000 bulbs during Dasara|Features a Golden Throne made of 200kg gold",
                category = "Palace",
                yearEstablished = 1912
            ),
            SiteEntity(
                id = "site_004",
                name = "Elephant Stables",
                descriptionEn = "A magnificent suite of eleven domed chambers built to house the royal elephants of the Vijayanagara kings. Its graceful arches and detailed facades reveal the empire’s love for elegant utility and grand architecture.",
                descriptionKn = "ವಿಜಯನಗರ ಸಾಮ್ರಾಜ್ಯದ ಹನ್ನೊಂದು ರಾಜ ಆನೆಗಳನ್ನು ಇರಿಸಲು ಬಳಸುತ್ತಿದ್ದ ಭವ್ಯ ಕಟ್ಟಡ.",
                shortDescription = "Magnificent royal stables for 11 elephants",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/Elephant_Stables_Hampi.jpg/1200px-Elephant_Stables_Hampi.jpg",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                latitude = 15.3311,
                longitude = 76.4795,
                hiddenFacts = "Contains 11 large domed chambers|Features Indo-Islamic style|Central dome is the most ornate",
                category = "Monument",
                yearEstablished = 1500
            ),
            SiteEntity(
                id = "site_005",
                name = "Gol Gumbaz",
                descriptionEn = "The mausoleum of King Mohammed Adil Shah, Gol Gumbaz is famed for its enormous unsupported dome and the Whispering Gallery. Visitors marvel at the dramatic acoustics and the massive stone terraces that surround this Bijapur landmark.",
                descriptionKn = "ಮೊಹಮ್ಮದ್ ಆದಿಲ್ ಶಾಹಿಯ ಸಮಾಧಿ, ವಿಶ್ವದ ಎರಡನೇ ಅತಿದೊಡ್ಡ ಗುಮ್ಮಟ ಹೊಂದಿದೆ.",
                shortDescription = "Mausoleum with the world's second largest dome",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0c/Gol_Gumbaz_Bijapur_Karnataka.jpg/1200px-Gol_Gumbaz_Bijapur_Karnataka.jpg",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                latitude = 16.8302,
                longitude = 75.7351,
                hiddenFacts = "Whispering Gallery reflects sound 7-10 times|Built using dark grey basalt|Dome is unsupported by pillars",
                category = "Monument",
                yearEstablished = 1656
            ),
            SiteEntity(
                id = "site_006",
                name = "Badami Cave Temples",
                descriptionEn = "A cluster of four rock-cut cave temples carved into soft red sandstone, Badami preserves exquisite sculptures of Shiva, Vishnu, and Jain Tirthankaras. The cave reliefs and pillared halls reflect early Chalukyan craftsmanship and religious harmony.",
                descriptionKn = "6ನೇ ಶತಮಾನದ ಮರಳುಗಲ್ಲಿನ ಬಂಡೆಗಳಲ್ಲಿ ಕೆತ್ತಲಾದ ಭವ್ಯ ಗುಹಾ ದೇವಾಲಯಗಳು.",
                shortDescription = "Exquisite 6th-century sandstone cave temples",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3a/Badami_Cave_Temple_No_1.jpg/1200px-Badami_Cave_Temple_No_1.jpg",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                latitude = 15.9114,
                longitude = 75.6766,
                hiddenFacts = "Features an 18-armed dancing Shiva|Four separate caves representing different faiths|Overlooks ancient Agastya Lake",
                category = "Temple",
                yearEstablished = 540
            ),
            SiteEntity(
                id = "site_007",
                name = "Belur Chennakesava Temple",
                descriptionEn = "A masterpiece of Hoysala architecture, Belur Temple dazzles with polished stone sculptures and ornate friezes. Its walls depict dancers, deities, and scenes from the epics, making it a highlight of medieval Karnataka craftsmanship.",
                descriptionKn = "ಹೊಯ್ಸಳ ವಾಸ್ತುಶಿಲ್ಪದ ಅದ್ಭುತ ಕೃತಿ, ರಾಜ ವಿಷ್ಣುವರ್ಧನ ನಿರ್ಮಿಸಿದ ದೇವಾಲಯ.",
                shortDescription = "Masterpiece of Hoysala stone carving",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1b/Chennakeshava_Temple_Belur_India.jpg/1200px-Chennakeshava_Temple_Belur_India.jpg",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                latitude = 13.1624,
                longitude = 75.8596,
                hiddenFacts = "Took 103 years to complete|Contains 48 unique pillars|Famous for bracket figures (Madanikas)",
                category = "Temple",
                yearEstablished = 1117
            ),
            SiteEntity(
                id = "site_008",
                name = "Shravanabelagola",
                descriptionEn = "A major Jain pilgrimage site centered on the 57-foot monolithic statue of Lord Bahubali. The hilltop statue, ancient inscriptions, and serene surroundings make it a revered destination for spiritual seekers.",
                descriptionKn = "57 ಅಡಿ ಎತ್ತರದ ಏಕಶಿಲಾ ಬಾಹುಬಲಿ ಪ್ರತಿಮೆಯನ್ನು ಹೊಂದಿರುವ ಪವಿತ್ರ ಜೈನ ಯಾತ್ರಾ ಸ್ಥಳ.",
                shortDescription = "World's tallest monolithic statue",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/17/Gomateshwara_Statue_Shravanabelagola.jpg/1200px-Gomateshwara_Statue_Shravanabelagola.jpg",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                latitude = 12.8573,
                longitude = 76.4862,
                hiddenFacts = "Carved from a single block of granite|Mahamastakabhisheka held every 12 years|Visible from 30 km away",
                category = "Temple",
                yearEstablished = 981
            ),
            SiteEntity(
                id = "site_009",
                name = "Pattadakal",
                descriptionEn = "A UNESCO World Heritage site where northern Nagara and southern Dravidian styles harmonize in stone. The temple complex showcases exquisite carvings, ceremonial gateways, and royal chapels from the Chalukya era.",
                descriptionKn = "7 ಮತ್ತು 8ನೇ ಶತಮಾನದ ಹಿಂದೂ ಮತ್ತು ಜೈನ ದೇವಾಲಯಗಳ ಸಂಕೀರ್ಣ, ಯುನೆಸ್ಕೋ ತಾಣ.",
                shortDescription = "Harmonious blend of North and South Indian architecture",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8b/Pattadakal_Group_of_Monuments.jpg/1200px-Pattadakal_Group_of_Monuments.jpg",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                latitude = 15.9491,
                longitude = 75.8169,
                hiddenFacts = "Used for coronation of Chalukya kings|Mix of Nagara and Dravidian styles|Contains 10 major temples",
                category = "Monument",
                yearEstablished = 680
            ),
            SiteEntity(
                id = "site_010",
                name = "Aihole (Durga Temple)",
                descriptionEn = "Aihole is known as the cradle of Indian rock architecture, with over 125 temples and monuments. The Durga Temple’s apsidal form and carvings show how early Chalukyan designers experimented with temple styles.",
                descriptionKn = "ಭಾರತೀಯ ದೇವಾಲಯ ವಾಸ್ತುಶಿಲ್ಪದ ತೊಟ್ಟಿಲು ಎಂದು ಕರೆಯಲ್ಪಡುವ ಐಹೊಳೆ.",
                shortDescription = "The cradle of Indian rock architecture",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/52/Durga_Temple_Aihole_2.jpg/1200px-Durga_Temple_Aihole_2.jpg",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                latitude = 16.0211,
                longitude = 75.8825,
                hiddenFacts = "Durga temple has unique apsidal plan|Over 125 temples in the village|First capital of Chalukyas",
                category = "Temple",
                yearEstablished = 450
            ),
            SiteEntity(
                id = "site_011",
                name = "Murudeshwar Temple",
                descriptionEn = "Perched beside the Arabian Sea, Murudeshwar Temple is dominated by a towering Shiva statue and a dramatic 20-story gopuram. The seaside setting and temple architecture create one of Karnataka’s most striking coastal pilgrimages.",
                descriptionKn = "ಅರಬ್ಬೀ ಸಮುದ್ರದ ತೀರದಲ್ಲಿರುವ ವಿಶ್ವದ ಎರಡನೇ ಅತಿದೊಡ್ಡ ಶಿವನ ಪ್ರತಿಮೆ.",
                shortDescription = "Gigantic Shiva statue by the Arabian Sea",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/69/Murudeshwar_Shiva_1.jpg/1200px-Murudeshwar_Shiva_1.jpg",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                latitude = 14.0942,
                longitude = 74.4842,
                hiddenFacts = "Shiva statue is 123 feet tall|Gopuram is 20 stories high|Surrounded by sea on three sides",
                category = "Temple",
                yearEstablished = 2002
            ),
            SiteEntity(
                id = "site_012",
                name = "Chitradurga Fort",
                descriptionEn = "A massive hill fort with seven concentric rings of defense, Chitradurga is famed for its stone ramparts, secret reservoirs, and the legendary bravery of Onake Obavva. It remains one of Karnataka’s most fortified and mysterious medieval sites.",
                descriptionKn = "ಚಿತ್ರದುರ್ಗದ ಏಳು ಸುತ್ತಿನ ಕೋಟೆ, ಅಪ್ರತಿಮ ರಕ್ಷಣಾ ವ್ಯವಸ್ಥೆಗೆ ಪ್ರಸಿದ್ಧ.",
                shortDescription = "The invincible Seven-Ringed Fort",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cf/Chitradurga_Fort_Overview.jpg/1200px-Chitradurga_Fort_Overview.jpg",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                latitude = 14.2230,
                longitude = 76.4020,
                hiddenFacts = "Never captured by direct assault|Contains 18 ancient temples|Legend of Onake Obavva",
                category = "Monument",
                yearEstablished = 1000
            ),
            SiteEntity(
                id = "site_013",
                name = "Bidar Fort",
                descriptionEn = "A 14th-century citadel famous for its Persian-style palaces, tile work, and ingenious Karez water system. Bidar Fort reveals the cosmopolitan grandeur of the Bahmani and Barid Shahi courts.",
                descriptionKn = "ಬಹಮನಿ ಸುಲ್ತಾನರ ಭವ್ಯ ಕೋಟೆ, ಪರ್ಷಿಯನ್ ವಾಸ್ತುಶಿಲ್ಪದ ಸಾಕ್ಷ್ಯ.",
                shortDescription = "Bahmani era fort with Persian influence",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Bidar_Fort_Entrance.jpg/1200px-Bidar_Fort_Entrance.jpg",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                latitude = 17.9250,
                longitude = 77.5300,
                hiddenFacts = "Famous Karez water system|Beautiful tile work in Rangeen Mahal|Fort wall is 5.5 km long",
                category = "Monument",
                yearEstablished = 1427
            ),
            SiteEntity(
                id = "site_014",
                name = "Hampi Bazaar",
                descriptionEn = "The bustling market street of Vijayanagara, Hampi Bazaar was once lined with shops selling gems, spices, and royal goods. Today its ruins evoke the city’s commercial glory and invite visitors to wander through historic colonial-style pavilions.",
                descriptionKn = "ವಿಜಯನಗರ ಕಾಲದ ವಜ್ರ ಮತ್ತು ಕುದುರೆಗಳ ಐತಿಹಾಸಿಕ ಮಾರುಕಟ್ಟೆ.",
                shortDescription = "Ancient marketplace of the Vijayanagara Empire",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d3/Hampi_Bazaar_from_Matanga_Hill.jpg/1200px-Hampi_Bazaar_from_Matanga_Hill.jpg",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                latitude = 15.3350,
                longitude = 76.4750,
                hiddenFacts = "Stretched over 700 meters|Lined with colonnaded pavilions|Used for royal processions",
                category = "Historical Market",
                yearEstablished = 1336
            ),
            SiteEntity(
                id = "site_015",
                name = "Jog Falls",
                descriptionEn = "One of India’s most spectacular waterfalls, Jog Falls plunges 830 feet in four distinct cascades. Surrounded by forested hills and misty cliffs, it is a dramatic natural landmark in the Western Ghats.",
                descriptionKn = "ಶರಾವತಿ ನದಿಯಿಂದ ಉಂಟಾದ ಭಾರತದ ಎರಡನೇ ಅತಿದೊಡ್ಡ ಜಲಪಾತ.",
                shortDescription = "Majestic waterfall in the Western Ghats",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b3/Jog_Falls_Karnataka_India.jpg/1200px-Jog_Falls_Karnataka_India.jpg",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                latitude = 14.2285,
                longitude = 74.8116,
                hiddenFacts = "Consists of four cascades: Raja, Rani, Roarer, Rocket|Highest waterfall in Karnataka",
                category = "Historical Site",
                yearEstablished = 0
            )
        )
    }
}
