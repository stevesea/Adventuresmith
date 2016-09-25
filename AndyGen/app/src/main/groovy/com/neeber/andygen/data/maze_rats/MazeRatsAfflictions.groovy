package com.neeber.andygen.data.maze_rats

import com.neeber.andygen.data.Shuffler
import com.neeber.andygen.data.AbstractGenerator

import javax.inject.Inject

class MazeRatsAfflictions extends AbstractGenerator {
    static final List<String> afflictions = """\
Ages backwards
Always honest
Always whispers
Babbling
Bleeds seawater
Blurry vision
Can only caw
Can’t stop singing
Caveman speech
Chameleon Eyes
Colorblind
Coma
Drooling
Drunkenness
Dyscalculia
Dyslexia
Inner meltdown
Eyes on stalks
Faceblind
Falls in love
Fast hair growth
Feverish
Fish Eyes
Floats 1” off ground
Followed by birds
Forked tongue
Full body numbness
Gains 2d100 pounds
Gains d20” height
Gender swap
Gill Slits
Goat eyes
Goat legs
Gorgon hair
Grows a beak
Grows antlers
Grows cat tail
Grows feathers
Grows horns
Grows old
Grows scales
Grows second face
Grows shaggy fur
Grows back-spines
Grows tusks
Hair falls out
Hallucinations
Hands swell up
Hands to crab claws
Hands to talons
Insomnia
Invisible Eyes
Invisible Head
Kleptomania
Language replaced
Limbs to tentacles
Literal third eye
Lizard eyes
Loses d20” height
Mead snob
Monkey tail
Mouth smokes
Must shed skin
Mute
Narcolepsy
No new memories
No sense of direction
No sense of time
No taste buds
Nocturnal
One leg grows d6”
Owl eyes
Pacifist
Purple skin
Random animal head
Says thoughts aloud
Scorpion tail
Second personality
See-through skin
Shouts everything
Silly walk
Skin boils
Skin sags
Skips everywhere
Skull grows
Slightly translucent
Slimy skin
Smells like fish
Snake tail
Stuttering
Suckers on hands
Terrible taste in art
The shakes
Tinnitus
Tone-deaf
Transparent skin
Vegetarianism
Voice swaps gender
Voice echoes
Webbedd hands/feet\
""".readLines()

    @Inject
    MazeRatsAfflictions(Shuffler shuffler) {
        super(shuffler);
    }

    List<GString> getFormatters() {
        return [
                "${ -> pick(afflictions)}",
        ]
    }

}
