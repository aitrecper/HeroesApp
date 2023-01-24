package recio.aitor.heroesapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Superhero (val superHeroName: String, val alterEgoName: String, val bio: String, val power: Float) :
    Parcelable {


}