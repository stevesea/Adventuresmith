package com.neeber.andygen.data.freebooters_on_the_frontier

import com.neeber.andygen.data.Shuffler
import com.neeber.andygen.data.AbstractGenerator

import javax.inject.Inject

class FotFTraits extends AbstractGenerator {
    static final List<String> virtues = """\
Ambitious
Benevolent
Bold
Brave
Charitable
Chaste
Cautious
Compassionate
Confident
Considerate
Cooperative
Courteous
Creative
Curious
Daring
Defiant
Dependable
Determined
Disciplined
Enthusiastic
Fair
Focused
Forgiving
Friendly
Frugal
Funny
Generous
Gregarious
Helpful
Honest
Honorable
Hopeful
Humble
Idealistic
Just
Kind
Loving
Loyal
Merciful
Orderly
Patient
Persistent
Pious
Resourceful
Respectful
Responsible
Selfless
Steadfast
Tactful
Tolerant\
""".readLines()

    static final List<String> vices = """\
Addict
Aggressive
Alcoholic
Antagonistic
Arrogant
Boastful
Cheater
Covetous
Cowardly
Cruel
Decadent
Deceitful
Disloyal
Doubtful
Egotistical
Envious
Gluttonous
Greedy
Hasty
Hedonist
Impatient
Inflexible
Irritable
Lazy
Lewd
Liar
Lustful
Mad
Malicious
Manipulative
Merciless
Moody
Murderous
Obsessive
Petulant
Prejudiced
Reckless
Resentful
Rude
Ruthless
Self-pitying
Selfish
Snobbish
Stingy
Stubborn
Vain
Vengeful
Wasteful
Wrathful
Zealous\
""".readLines()

    @Inject
    FotFTraits(Shuffler shuffler) {
        super(shuffler);
    }

    List<GString> getFormatters() {
        return [
                "${ -> pick(virtues)} <> ${ -> pick(vices)}",
        ]
    }

}
