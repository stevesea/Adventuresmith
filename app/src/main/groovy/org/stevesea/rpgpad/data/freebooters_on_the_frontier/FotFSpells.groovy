/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of RPG-Pad.
 *
 * RPG-Pad is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RPG-Pad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RPG-Pad.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.stevesea.rpgpad.data.freebooters_on_the_frontier

import com.samskivert.mustache.Mustache
import groovy.transform.CompileStatic
import org.stevesea.rpgpad.data.AbstractGenerator

@CompileStatic
class FotFSpells extends AbstractGenerator {
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

    String getElement() {
        pick(elements)
    }

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
    String getForm() {
        pick(forms)
    }

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
    String getAdjective() {
        pick(adjectives)
    }

    static final List<String> name_1stparts = """
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

    static final List<String> name_2ndparts = """
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
    String getName1stPart() {
        pick(name_1stparts)
    }
    String getName2ndPart() {
        pick(name_2ndparts)
    }
    String getPossibleHyphen() {
        List<String> oneInTenchanceOfHyphen = ['-']
        oneInTenchanceOfHyphen.addAll(Collections.nCopies(9, ''))
        pick(oneInTenchanceOfHyphen)
    }

    List<String> templates = [
            '{{element}} {{form}}',
            '{{adjective}} {{form}}',
            '{{adjective}} {{element}}',
            '{{form}} of {{element}}',
            '{{form}} of the {{adjective}} {{form}}',
            '{{form}} of {{adjective}} {{element}}',
            '{{name1stPart}}{{possibleHyphen}}{{name2ndPart}}’s {{adjective}} {{form}}',
            '{{name1stPart}}{{possibleHyphen}}{{name2ndPart}}’s {{adjective}} {{element}}',
            '{{name1stPart}}{{possibleHyphen}}{{name2ndPart}}’s {{form}} of {{element}}',
            '{{name1stPart}}{{possibleHyphen}}{{name2ndPart}}’s {{element}} {{form}}',
            '{{form}} of {{name1stPart}}{{possibleHyphen}}{{name2ndPart}}',
            '{{element}} {{form}} of {{name1stPart}}{{possibleHyphen}}{{name2ndPart}}',
            '{{adjective}} {{form}} of {{name1stPart}}{{possibleHyphen}}{{name2ndPart}}',
            '{{adjective}} {{element}} of {{name1stPart}}{{possibleHyphen}}{{name2ndPart}}',
    ]

    String getTemplate() {
        pick(templates)
    }

    String generate() {
        Mustache.compiler().compile(getTemplate()).execute(this)
    }

}
