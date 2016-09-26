package org.steavesea.rpg_boy2000.data.freebooters_on_the_frontier

import org.steavesea.rpg_boy2000.data.AbstractGenerator
import org.steavesea.rpg_boy2000.data.Shuffler
import org.steavesea.rpg_boy2000.data.RpgBoyData

import javax.inject.Inject

class FotFSpells extends AbstractGenerator {
    public String getName() {
        return "Spells"
    }
    public String getDataset() {
        return RpgBoyData.FREEBOOTERS
    }
    static final List<String> elements = """
        Acid Aether Air Anger Ash Avarice
        Balance Blight Blood Bone Bones Brimstone
        Clay Cloud Copper Cosmos
        Dark Death Deceit Despair Dimension Doom Dust
        Earth Ember Energy Envy
        Fear Fire Fog Force Fury
        Glory Gluttony Gold Greed
        Hate Hatred Health Heat History Hope
        Ice Iron
        Justice
        Knowledge
        Lead Lies Life Light Lightning Lore Love Lust
        Metal Might Mist Moon Mud
        Nature
        Oil
        Pain Perception Plane Plant Poison
        Quicksilver
        Revulsion Rot
        Salt Shadow Sight Silver Smoke Soil Soul Souls Sound Spirit Stars Steam Steel Stone Storm Sun
        Terror Time Treasure Truth
        Vanity Venom Vigor Void
        Water Will Wind Wisdom Wood
        Youth
        """.tokenize()

    static final List<String> forms = """
        Armor Arrow Aura
        Bane Beast Blade Blast Blessing Blob Blood Bolt Bond Boon Brain Burst
        Call Charm Circle Claw Cloak Cone Crown Cube Cup Curse
        Dagger Dart Demon Disturbance Door
        Eye Eyes
        Face Fang Feast Finger Fissure Fist
        Gate Gaze Glamour Globe Golem Guard Guide Guise
        Halo Hammer Hand Heart Helm Horn
        Lock
        Mantle Mark Memory Mind Mouth
        Noose
        Oath Oracle
        Pattern Pet Pillar Pocket Portal Pyramid
        Ray Rune
        Scream Seal Sentinel Servant Shaft Shield Sigil Sign Song Spear Spell Sphere Spray Staff Storm Strike Sword
        Tendril Tongue Tooth Trap
        Veil Voice
        Wall Ward Wave Weapon Weave Whisper Wings Word
        """.tokenize()

    static final List<String> adjectives = """
        All-Knowing All-Seeing Arcane
        Befuddling Binding Black Blazing Blinding Bloody Bright
        Cacophonous Cerulean Concealing Confusing Consuming Crimson
        Damnable Dark Deflecting Delicate Demonic Devastating Devilish Diminishing Draining
        Eldritch Empowering Enlightening Ensorcelling Entangling Enveloping Erratic Evil Excruciating Expanding Extra-Planar
        Fearsome Flaming Floating Freezing
        Glittering Gyrating
        Helpful Hindering
        Icy Illusory Incredible Inescapable Ingenious Instant Invigorating Invisible Invulnerable
        Liberating
        Maddening Magnificent Many-Colored Mighty Most-Excellent
        Omnipotent Oozing
        Penultimate Pestilential Piercing Poisonous Prismatic Raging
        Rejuvenating Restorative
        Screaming Sensitive Shimmering Shining Silent Sleeping Slow Smoking Sorcerer’s Strange Stupefying
        Terrible Thirsty Thundering Trans-Dimensional Transmuting
        Ultimate Uncontrollable Unseen Unstoppable Untiring
        Vengeful Vexing Violent Violet Viridian Voracious
        Weakening White Wondrous
        Yellow
        """.tokenize()

    static final List<String> name_1stpart = """
        AAb Aga Alha Appol Apu Arne Asmo
        Baha Bal Barba Bol By
        Can Cinni Cir Cyn Cyto Dar Darg De Des Dra Dul
        Elez Ely Ez
        Fal Faral Flo Fol
        Gaili Garg Gast Gil Gy
        Haz Heca Her Hog Hur
        IIk Ilde In
        Jas Jir Ju
        Krak Kul
        Laf Long
        Ma Mer Mercu Mor Mune Munno Murz
        Naf
        OOsh
        Pande Pander Par Per
        Quel
        Ra Ragga Rhi
        Satan Satur Semi Sera She Shrue Sloo Sol
        T’ Tcha Tol Tub Tur
        UVag
        Val Vance Ver Vish
        Wa Win
        Xa
        Yu
        Za Zal Zan Zili Zim Zuur Zza
        """.tokenize()

    static final List<String> name_2ndpart = """
        ak alto ana anti aris ark asta
        balia bus by
        cas ce
        derol deus din dok dor dred driar dula dun dustin
        er
        fant fia fonse
        gad gax glana goria goth
        heer houlik
        ia iala iana ingar ista
        jan jobulon
        kan kang konn
        lah leius leo leou lin lonia lonius loo lume
        ma mas mast mia miel motto moulian mut
        nak nia nish nob
        o ol ool
        pa pheus phim por
        quint ramis rezzin ro rrak ry
        sira sta
        te teria thakk thalon tine toomb torr troya tur tuva
        u
        valva vance vilk
        wink
        xa
        yop
        zant zark zirian zred
        """.tokenize()

    @Inject
    FotFSpells(Shuffler shuffler) {
        super(shuffler);
    }

    GString getWizardName() {
        List<String> oneInTenchanceOfHyphen = ["-"]
        oneInTenchanceOfHyphen.addAll(Collections.nCopies(9, ""))

        return "${ -> pick(name_1stpart)}${ -> pick(oneInTenchanceOfHyphen)}${ -> pick(name_2ndpart)}"
    }

    List<GString> getNoNameFormatters() {
        return [
                "${ -> pick(elements)} ${ -> pick(forms)}",
                "${ -> pick(adjectives)} ${ -> pick(forms)}",
                "${ -> pick(adjectives)} ${ -> pick(elements)}",
                "${ -> pick(forms)} of ${ -> pick(elements)}",
                "${ -> pick(forms)} of the ${ -> pick(adjectives)} ${ -> pick(forms)}",
                "${ -> pick(forms)} of ${ -> pick(adjectives)} ${ -> pick(elements)}"
        ]
    }

    List<GString> getNameFormatters() {
        return [
                "${ -> getWizardName()}’s ${ -> pick(adjectives)} ${ -> pick(forms)}",
                "${ -> getWizardName()}’s ${ -> pick(adjectives)} ${ -> pick(elements)}",
                "${ -> getWizardName()}’s ${ -> pick(forms)} of ${ -> pick(elements)}",
                "${ -> getWizardName()}’s ${ -> pick(elements)} ${ -> pick(forms)}",
                "${ -> pick(forms)} of ${ -> getWizardName()}",
                "${ -> pick(elements)} ${ -> pick(forms)} of ${ -> getWizardName()}",
                "${ -> pick(adjectives)} ${ -> pick(forms)} of ${ -> getWizardName()}",
                "${ -> pick(adjectives)} ${ -> pick(elements)} of ${ -> getWizardName()}",
        ]
    }

    List<GString> getFormatters() {
        return getNoNameFormatters() + getNameFormatters()
    }

}
