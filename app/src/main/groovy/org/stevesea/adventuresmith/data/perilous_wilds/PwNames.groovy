/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of Adventuresmith.
 *
 * Adventuresmith is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Adventuresmith is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Adventuresmith.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.stevesea.adventuresmith.data.perilous_wilds

import groovy.transform.CompileStatic
import org.stevesea.adventuresmith.data.AbstractGenerator;

@CompileStatic
class PwNames extends AbstractGenerator {
    LinkedHashMap<String, LinkedHashMap<String, List<String>>> names = [
            Arpad: [
                    male: """\
Agoston
Arpad
Attila
Bognar
Denes
Edmond
Erno
Etele
Ferdinand
Florian
Geza
Gyula
Hugo
Karcsi
Konrad
Lazlo
Lukas
Marko
Miklos
Peti
Robi
Tamas
Ronold
Viktor
Zoltan\
""".readLines(),
                    female: """\
Abigel
Aliz
Amalia
Andrea
Aranka
Csilla
Edit
Erzebet
Gertrud
Greta
Iren
Kamilla
Lara
Lia
Lujza
Matild
Olga
Otilia
Panna
Roza
Terez
Tunda
Valeria
Vilma
Viola\
""".readLines(),
                    steading: """\
Aldott (Blessed)
Almahid (Applebridge)
Elesett (Fallen)
Feketz (Black Rock)
Godor (Pit)
Kelegaz (Eastford)
Kigyov (Snake Swamp)
Kiralokas (Queen’s Castle)
Kiralsir (King’s Grave)
Magziklar (Highcliff)
Mocsar (Fen)
Nagyvros (Hightown)
Okorm (Oxfield)
Orkfal (Orcwall)
Perov (Redwater)
Soterdo (Dark Wood)
Tehenvar (Cow Town)
Toron (Tower)
Torott (Ironhold)
Utolszer (Last Stand)
Valavolg (Greendale)
Vastar (Dwarf Watch)
Viz (Oxfield)
Volgyom (Valley)
Zoldom (Green Hill)\
""".readLines(),
                    mount: """\
Barat (Friend)
Barsony (Velvet)
Edesem (Sweetheart)
Egatz (Skyfire)
Eso (Rain)
Fakyla (Torch)
Feketsor (Black Mane)
Felho (Cloud)
Flotta (Fleet)
Gazda (Master)
Hold (Moon)
Igaslo (Dobbin)
Ijeda (Skittish)
Koszalo (Rambler)
Megmento (Savior)
Napfen (Sunshine)
Rozsa (Rose)
Szamla (Bill)
Szellem (Spritied)
Szeplok (Freckles)
Szerence (Lucky)
Tusko (Stumps)
Vad (Wild One)
Vihar (Storm)
Villam (Lightning)\
""".readLines(),
            ],
            Oloru: [
                    male: """\
Adibemi
Aboye
Adegoke
Ayokunle
Babajide
Babatunde
Enitan
Femi
Kayin
Kayode
Lanre
Lekan
Mongo
Nwachukwu
Oban
Ogun
Olukayode
Oluwalanni
Oluwatoke
Onipede
Sijuade
Toben
Utiba
Zaki
Zoputan\
""".readLines(),
                    female: """\
Abeni
Ade
Alaba
Bolanle
Bosade
Daraja
Fari
Gbemisola
Ife
Ige
Lewa
Mojisola
Monifa
Olufemi
Omolara
Oni
Orisa
Osa
Ronke
Shanum
Simisola
Titlayo
Yejide
Yewande
Zauna\
""".readLines(),
                    steading: """\
Asala Ilu (Desert Town)
Atijo Ina (Old Fire)
Bajesia (Broken Banner)
Dudu Olomi (Blackmarsh)
Ebutte Meta (Three Ports)
Ejodo (Snake River)
Esukale (Devil’s Dinner)
Fadormi (Silver Spring)
Funfumi (Whitewater)
Gooluna (Gold Road)
Ijisofo (Storm Hollow)
Ikukenu (Dearth’s Door)
Iwin Ago (Faery Watch)
Jinibi (Far Place)
Oba Ile (King’s Home)
Oduroke (Prayer Hill)
Ogbinibi (Farming Place)
Ogunibi (Battle Place)
Okanigi (One Tree)
Okutasibo (Stone Marker)
Olorusura (God’s Treasure)
Olusajeki (Wizard’s Keep)
Oluwakaji (Lord’s Tomb)
Opolokuta (Many Stones)
Opoligi (Many Trees)\
""".readLines(),
                    mount: """\
Adiitu (Mystery)
Alayo (Happy)
Atale (Ginger)
Bilu (Bill)
Dudupatak (Dark Hoof)
Egun (Bramble)
Eniyan (Ember)
Esirun (Longshanks)
Fenuko (Kiss)
Funfungo (White Hair)
Gunirun (Longhair)
Imole (Bright)
Imole Uju (Bright Eye)
Ira (Rambler)
Itan (Story)
Iyebiye (Precious)
Lulu (Powder)
Nilera (Healthy)
Ogbo (Spotted)
Ogboju (Brave Heart)
Ojiji (Shadow)
Olooto (Faithful)
Orisa (Spring)
Orun (Heaven)
Sare (Flash)\
""".readLines(),
            ],
            Valkoina: [
                    male: """\
Aatami
Armas
Arsi
Arvi
Eetu
Hannu
Heimo
Ilkka
Jorma
Kaapo
Kain
Kauko
Lari
Manu
Nuutti
Petri
Raimo
Reima
Risto
Sakari
Sampsa
Seppo
Taito
Terho
Vilppu\
""".readLines(),
                    female: """\
Aija
Aina
Ainikki
Heini
Ilona
Irja
Jaana
Kirsi
Maija
Marita
Miina
Mimmi
Minja
Mira
Naemi
Outi
Pirjo
Päivikki
Riikka
Saimi
Suoma
Suvi
Tuula
Vellamo
Virpi\
""".readLines(),
                    steading: """\
Etuvartio (Outpost)
Hopea Kaivos (Silver Mine)
Kalapunki (Fish Town)
Kivimurri (Stone Wall)
Maaginen (Magic)
Maki Linna (Hill Castle)
Merenranta (Seaside)
Metsästysmaat (Hunting Ground)
Mustakota (Black Hut)
Mäenrinne (Hillside)
Paja (Forge)
Pienni Paikka (Low Place)
Pyhä Paikka (Holy Place)
Rantakallio (Cliff)
Rikki (Broken)
Suo (Swamp)
Suosi (Favored)
Torni (Tower)
Turvapaikka (Refuge)
Uusipunki (New Town)
Valkoinen Kivi (Whitestone)
Valtaistuin (Throne)
Vapaanki (Free Town)
Vihreä Paikka (Green Place)
Viimeinen Koti (Last Home)\
""".readLines(),
                    mount: """\
Aave (Ghost)
Enkeli (Angel)
Haiva (Shadow)
Hopea (Silver)
Ilmavirta (Current)
Kesi (Tame)
Kestaba (Durable)
Kiukuinnen (Angry)
Lansiviima (West Wind)
Luotettava (Trusty)
Myrskyisa (Stormy)
Nokka (Bill)
Noyra (Humble)
Pitkanena (Longnose)
Saikki (Skittish)
Salama (Thunderbolt)
Sankari (Hero)
Sisko (sister)
Upea (Magnificent)
Vahva (Strong)
Valkoinen (White)
Varmaotteinen (Surefoot)
Vesuri (Billhook)
Vinha (Fast)
Ystava (Friend)\
""".readLines(),
            ],
            Tamanarugan: [
                    male: """\
Ade
Adi
Amaziah
Ayokunle
Ary
Bambang
Bima
Budi
Darma
Dian
Eli
Gunardi
Hartono
Irwan
Lauwita
Manusama
Okan
Onesimus
Sammin
Tameem
Tanaya
Tirto
Wiryono
Yandi
Zebulun\
""".readLines(),
                    female: """\
Adah
Bulan
Candrakusuma
Devi
Hanjojo
Iman
Intan
Laksmini
Lestari
Limijanto
Marah
Megawati
Melati
Nadiyya
Ophrah
Ramza
Sapphiral
Selah
Suminten
Tamar
Tanjaya
Tjokro
Tri
Wangi
Zenze\
""".readLines(),
                    steading: """\
Airdib (Blessed Waters)
Airjinh (Clearwater)
Akhir Jalan (Road’s End)
Berdarah (Bloody)
Bidang Bera (Fallow Field)
Candibula (Moon Temple)
Ditingga (Forsaken)
Emasungai (Gold Creek)
Gunung (Mountain)
Kayu (Timber)
Kuil (Temple)
Ladang Hijau (Greenfield)
Lembah (Valley)
Menjau (Far Away)
Ngarai (Canyon)
Persimpangan (Crossroads)
Puncakit (Hilltop)
Sungairac (Poison River)
Teibing (Cliffside)
Tempat Aman (Safe Place)
Tempat Istir (Rest Place)
Terkutuk (Cursed)
Tersentu (Touched by God)
Wahah (Oasis)
Yangtinggi (High Tower)\
""".readLines(),
                    mount: """\
Anginu (North Wind)
Api (Blaze)
Bakat (Felicity)
Bakti (Loyal)
Beruntung (Lucky)
Bilah (Blade)
Biru (Blue)
Gemetar (Quiver)
Guntur (Thunder)
Hidungi (Black Nose)
Janda (Widow)
Kakicerah (Bright Foot)
Kakiring (Light Foot)
Keriangan (Sunshine)
Kunang (Firefly)
Lapar (Hungry)
Murni (Pure)
Paruh (Bill)
Penebus (Avenger)
Penyelemat (Savior)
Prajurit (Warrior)
Rusak (Broken)
Satucep (Fast One)
Setia (True Heart)
Tahanla (Durable)\
""".readLines(),
            ],
    ]
    Map<String, List<String>> locations = [
            Arpad: ['The Kingdom of Arpad', 'Based loosely on Hungarian'],
            Oloru: ['Oloru, Sky-God\'s Home', 'Based loosely on Yoruba'],
            Valkoina: ['Valkoina, Land of White Lion', 'Based loosely on Finnish'],
            Tamanarugan: ['The Tamanarugan Empire', 'Based loosely on Indonesian'],
    ]
    String generate(String location) {
        """\
${strong(locations.get(location)[0])} - ${small(locations.get(location)[1])}
<br/>
<br/>${ss('Male:')} ${pick(names.get(location).get('male'))}
<br/>${ss('Female:')} ${pick(names.get(location).get('female'))}
<br/>
<br/>${ss('Steading:')} ${pick(names.get(location).get('steading'))}
<br/>
<br/>${ss('Mount:')} ${pick(names.get(location).get('mount'))}
"""

    }

    String generate() {

    }

    public static Map<String, PwNames> generators = [
            Arpad: new PwNames() {
                @Override
                String generate() {
                    return generate('Arpad')
                }
            },
            Oloru:  new PwNames() {
                @Override
                String generate() {
                    return generate('Oloru')
                }
            },
            Valkoina:  new PwNames() {
                @Override
                String generate() {
                    return generate('Valkoina')
                }
            },
            Tamanarugan:  new PwNames() {
                @Override
                String generate() {
                    return generate('Tamanarugan')
                }
            },
    ]
}
