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
 *
 */


package org.stevesea.adventuresmith.data.freebooters_on_the_frontier

import groovy.transform.CompileStatic
import org.stevesea.adventuresmith.data.AbstractGenerator
import org.stevesea.adventuresmith.data.RangeMap

@CompileStatic
class FotFCharacters extends AbstractGenerator {
    List<String> genders = ['male', 'female']
    LinkedHashMap<String, LinkedHashMap<String, List<String>>> names = [
            Human: [
                    male : """\
Athelan
Aldred
Alger
Archard
Astyrian
Bowden
Brogan
Caden
Cerdic
Devan
Druce
Dugal
Edlyn
Ebis
Esward
Firman
Framar
Fugol
Garret
Gidwin
Gord
Govannon
Greme
Grindan
Halwen
Holt
Iden
Irbend
Kendrik
Leor
Lufian
Nyle
Odel
Ord
Orleg
Radan
Reged
Rowe
Scrydan
Seaver
Shepard
Snell
Stedman
Swift
Teon
Tobrec
Tredan
Ware
Warian
Wulf\
""".readLines(),
                    female : """\
Acca
Alodia
Andessa
Anlis
Ara
Ardith
Berroc
Bernia
Bodica
Brigantia
Brimlid
Caro
Cwen
Darel
Dawn
Diera
Dotor
Eda
Elene
Elga
Elswyth
Elva
Elvina
Erlina
Esma
Faradan
Freya
Garmang
Gloris
Harmilla
Hunnar
Juliana
Kandara
Laralan
Lorn
Maida
Megdas
Mercia
Mora
Ogethas
Ossia
Pallas
Rathet
Sibley
Sunnivar
Tate
Udela
Viradeca
Wilona
Zora\
""".readLines(),
            ],
            Halfling: [
                    male: """\
Adaman
Adelard
Adred
Agilward
Arnest
Balbas
Barton
Bell
Banco
Bowman
Cal
Emmet
Erling
Fastman
Foda
Freebern
Frid
Gerd
Hadred
Hagar
Halbert
Hamfast
Hildred
Huge
Isen
Jaco
Jungo
Helm
Konner
Lambert
Leon
Linus
Marko
Matti
Mekel
Melchior
Lesser
Nenko
Nob
Olo
Ortwin
Otto
Paladin
Pasco
Quintus
Sifro
Ted
Tolman
Wilber
Wiseman\
""".readLines(),
                    female: """\
Adelle
Agilward
Alfreda
Amalinde
Balba
Bella
Beryl
Bess
Camelia
Cordelia
Daisy
Demona
Drogga
Elanor
Ella
Elsbeth
Elsina
Emerly
Foda
Gilda
Gilly
Hanna
Hilda
Hildred
Janna
Jilly
Kat
Klare
Lily
Lobelia
Lorna
Lucie
Magda
Marga
Mari
Marigold
Marka
Marlyn
Mina
Noba
Olga
Ottillia
Pansy
Pervinca
Poppy
Rose
Rowan
Salina
Tella
Ulrica\
""".readLines(),
            ],
            Dwarf: [
                    male: """\
Bagan
Banar
Belir
Besil
Boran
Darin
Dirin
Doibur
Doigan
Fagan
Fignus
Firin
Gesil
Glagan
Glasil
Glenus
Goirin
Gosil
Hanar
Heran
Hoibur
Hoili
Hoinar
Holir
Homli
Kimli
Koisin
Lasin
Legan
Loilir
Mirin
Moli
Nasil
Nefur
Neli
Nignar
Noifur
Ramli
Regnar
Safur
Sali
Saran
Segnar
Serin
Simli
Tasil
Teli
Tisin
Toilin
Toinus\
""".readLines(),
                    female: """\
Berin
Bibura
Bisil
Dagna
Delinia
Deris
Dira
Disia
Dorinda
Faran
Fasina
Fignis
Foifur
Foimli
Gerda
Gestis
Ginus
Glegna
Glelia
Glelis
Glemlia
Gloigas
Gloigna
Glonara
Hegna
Hignara
Hoimlis
Kana
Kemlir
Keri
Keris
Kilina
Kolina
Korana
Lifur
Loilis
Loilina
Mamli
Milina
Moibur
Moli
Noris
Nosi
Rana
Ribura
Sasilia
Soirina
Soran
Toigna
Tomlis\
""".readLines(),
            ],
            Elf: [
                    male: """\
Amánd
Amioril
Analad
Anin
Anumir
Calithrambor
Calóng
Calór
Cebrin
Cóldor
Corfindil
Delithran
Elithranduil
Elverion
Eowóril
Galithrar
Gelith
Gladriendil
Glamir
Glarang
Glil-Gang
Glundil
Gorfilas
Góriand
Hal
Harang
Isil-Galith
Isilith
Isónd
Isorfildur
Legaraldur
Lómebrildur
Mil-Gan
Náldur
Nelith
Niol
Porfindel
Ráldur
Silmandil
Tand
Taralad
Tararion
Tendil
Téril
Tildur
Tiniomir
Unálad
Unebrin
Unéndil
Uriong\
""".readLines(),
                    female: """\
Amidë
Anadriedia
Anarania
Anebriwien
Anilmadith
Beliniel
Calararith
Cebridith
Celénia
Celil-Gathiel
Cidien
Eäróndra
Eärorfindra
Eláthien
Eláviel
Eleniel
Elorfindra
Elváwien
Eoweclya
Eowodia
Fórith
Gilmadith
Gladrieclya
Glélindë
Gorfinia
Hadrieviel
Haniel
Hebriclya
Legithralia
Lómithrania
Meclya
Mélith
Módien
Paclya
Paradien
Pedith
Pil-Gandra
Pirith
Porficlya
Sithralindë
Thrédith
Thrilmadith
Thrithien
Throrfindra
Tilmaclya
Tilmawen
Tinilmania
Uradriethiel
Urithrarith
Urorfiviel\
""".readLines(),
            ],

    ]

    RangeMap playbooks = new RangeMap()
        .with(1..6, 'Fighter')
        .with(7..9, 'Thief')
        .with(10..11, 'Cleric')
        .with(12, 'Magic-User')

    Map<String, RangeMap> heritages = [
            Thief: new RangeMap()
                    .with(1..7, 'Human')
                    .with(8..10,'Halfling')
                    .with(11, 'Dwarf')
                    .with(12, 'Elf'),
            'Magic-User': new RangeMap()
                    .with(1..8, 'Human')
                    .with(9,'Halfling')
                    .with(10, 'Dwarf')
                    .with(11..12, 'Elf'),
            'Cleric': new RangeMap()
                    .with(1..7, 'Human')
                    .with(8,'Halfling')
                    .with(9..11, 'Dwarf')
                    .with(12, 'Elf'),
            'Fighter': new RangeMap()
                    .with(1..7, 'Human')
                    .with(8,'Halfling')
                    .with(9..11, 'Dwarf')
                    .with(12, 'Elf'),
    ]

    Map<String, RangeMap> alignments = [
            Thief: new RangeMap()
                    .with(1..2, 'Evil')
                    .with(3..6, 'Chaotic')
                    .with(7..10, 'Neutral')
                    .with(11..12, 'Good'),
            'Magic-User': new RangeMap()
                    .with(1..3, 'Evil')
                    .with(4..8, 'Chaotic')
                    .with(9..12, 'Good'),
            'Cleric': new RangeMap()
                    .with(1..3, 'Evil')
                    .with(4..5, 'Chaotic')
                    .with(6..7, 'Neutral')
                    .with(8..9, 'Lawful')
                    .with(10..12, 'Good'),
            'Fighter': new RangeMap()
                    .with(1..2, 'Evil')
                    .with(3..4, 'Chaotic')
                    .with(5..8, 'Neutral')
                    .with(9..10, 'Lawful')
                    .with(11..12, 'Good'),
    ]
    Map<String, RangeMap> gear1 = [
            Thief: new RangeMap()
                    .with(1..2, 'Knife')
                    .with(3..4, 'Dagger')
                    .with(5..6, 'Shortsword'),
            'Magic-User': new RangeMap()
                    .with(1..2, 'Magic wand')
                    .with(3..5, 'Magic staff')
                    .with(6, 'Arcane orb'),
            'Cleric': new RangeMap()
                    .with(1..2, 'Staff')
                    .with(3..4, 'Mace')
                    .with(5..6, 'Warhammer'),
            'Fighter': new RangeMap()
                    .with(1, 'Leather armor')
                    .with(2..5, 'Chainmail')
                    .with(6, 'Scale mail'),
    ]
    Map<String, RangeMap> gear2 = [
            Thief: new RangeMap()
                    .with(1..2, '3 throwing knives')
                    .with(3..4, 'Sling')
                    .with(5..6, 'Shortbow'),
            'Magic-User': new RangeMap()
                    .with(1..2, 'Bag of books')
                    .with(3, 'Dagger')
                    .with(4, 'Healing potion')
                    .with(5..6, 'Spell components'),
            'Cleric': new RangeMap()
                    .with(1..2, 'Shield')
                    .with(3..4, 'Leather armor')
                    .with(5..6, 'Chainmail'),
            'Fighter': new RangeMap()
                    .with(1, 'Healing potion')
                    .with(2, 'Shield')
                    .with(3, 'Poultices & herbs')
                    .with(4, 'Antitoxin')
                    .with(5, 'Rations')
                    .with(6, 'Adventuring gear'),
    ]

    Map<String, List<String>> appearances = [
            Fighter: """\
big feet
big mouth
big mustache
notable nose
braided hair
broken nose
chiseled
clear-eyed
cleft chin
crooked teeth
curly hair
dark skin
deep voice
dirty
earrings
gap-toothed
goatee
headband
high cheekbones
hirsute
lantern jaw
large ears
large hands
large head
long-legged
matted hair
missing ear
missing eye
missing finger
missing teeth
notable boots
notable helmet
perfect posture
pockmarked
raspy voice
rosy cheeks
sandals
scarred
tattoos
shaved head
smelly
smiling
squint
steely gaze
stubble
tattoos
unsmiling
well-scrubbed
youthful\
""".readLines(),
            Thief: """\
broken nose
chin whiskers
clean-shaven
clear-eyed
crooked teeth
curly hair
dark skin
deep voice
disfigured
disheveled
gap-toothed
gaunt
goatee
hirsute
hooded
limp
little mouth
long fingers
matted hair
missing eye
missing finger
missing teeth
narrowed eyes
notable footwear
notable gloves
notable cap/hat
notable nose
overbite
pale skin
pencil mustache
perfect posture
pockmarked
pointy chin
poor posture
raspy voice
ratty clothes
red-rimmed eyes
scarred
shifty eyes
small hands
smelly
squint
stubble
tattoos
unsmiling
unwashed
well-groomed
whispery voice
widow’s peak\
""".readLines(),
            Cleric: """\
big feet
blazing eyes
bushy eyebrows
circlet
clean-shaven
clear-eyed
cleft chin
crooked teeth
curly hair
dandruff
dark skin
dirty
earrings
gaunt
goatee
gray hair
headband
heavyset
high forehead
hirsute
hooded
large hands
long beard
missing teeth
miter
notable helmet
notable nose
notable garb
pale skin
perfect posture
perfumed
piercing gaze
pockmarked
rosy cheeks
scarred
shaved head
shining eyes
smelly
smiling
square chin
square-shouldered
strange marks
stubble
tattoos
thundering voice
tonsure
unwashed
warty
well-scrubbed\
""".readLines(),
            'Magic-User': """\
acid scars
aged
bald
black teeth
booming voice
burn scars
bushy eyebrows
chin whiskers
crooked teeth
curly hair
dark skin
disfigured
forked tongue
gaunt
glowing eyes
gnarled hands
goatee
gray hair
haggard
hairless
headband
high cheekbones
high forehead
hooded
limp
long beard
long fingernails
long hair
mismatched eyes
missing teeth
no eyebrows
notable nose
notable robes
oily skin
pale skin
pockmarked
pointy hat
poor posture
raspy voice
scarred
skeletal hands
skullcap
smelly
strange marks
sunken eyes
tattoos
unwashed
warty
white hair
widow’s peak\
""".readLines(),
    ]

    // hit die
    // mu d4
    // thief d6
    // cleric d8
    // fighter d10

    Map virtues_vices = [
            Evil: """\
${ss('Vices:')} ${ -> pickN(FotFTraits.vices, 3).join(', ')}
""",
            Chaotic: """\
${ss('Virtues:')} ${ -> pick(FotFTraits.virtues)}
<br/>${ss('Vices:')} ${ -> pickN(FotFTraits.vices, 2).join(', ')}
""",
            Neutral: """\
${ss('Virtues:')} ${ -> pick(FotFTraits.virtues)}
<br/>${ss('Vices:')} ${ -> pick(FotFTraits.vices)}
""",
            Lawful: """\
${ss('Virtues:')} ${ -> pickN(FotFTraits.virtues, 2).join(', ')}
<br/>${ss('Vices:')} ${ -> pick(FotFTraits.vices)}
""",
            Good: """\
${ss('Virtues:')} ${ -> pickN(FotFTraits.virtues, 3).join(', ')}
""",

    ]

    String generate() {
        def gender = pick(genders)
        def playbook = pick(playbooks)
        def heritage = pick(heritages.get(playbook))
        def alignment = pick(alignments.get(playbook))

        """\
<strong>${playbook}</strong> - ${pick(names.get(heritage).get(gender))} ${if (playbook == 'Magic-User') '(Wizname: ' + pick(FotFSpells.name_1stparts) + pick(FotFSpells.name_2ndparts) + ')' else ''}
<br/>${heritage}  ${gender} (${alignment})
<br/>
<br/>${ss('Strength:')} ${roll('3d6')}&nbsp;&nbsp;${ss('Dexterity:')} ${roll('3d6')}
<br/>${ss('Charisma:')} ${roll('3d6')}&nbsp;&nbsp;${ss('Constitution:')} ${roll('3d6')}
<br/>${ss('Wisdom:')} ${roll('3d6')}&nbsp;&nbsp;&nbsp;${ss('Intelligence:')} ${roll('3d6')}
<br/>${ss('Luck:')} ${roll('3d6')}
<br/>
<br/>${ss('Appearance:')} ${pickN(appearances.get(playbook), roll('1d2+1')).join(', ')}
<br/>
<br/>${virtues_vices.get(alignment)}
<br/>
<br/>${ss('Gear:')}
<br/>&nbsp;&nbsp;${pick(gear1.get(playbook))}
<br/>&nbsp;&nbsp;${pick(gear2.get(playbook))}
"""
    }

}
