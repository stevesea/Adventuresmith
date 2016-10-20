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

package org.stevesea.rpgpad.data.stars_without_number

import com.samskivert.mustache.Mustache
import groovy.transform.Canonical
import groovy.transform.CompileStatic
import org.stevesea.rpgpad.data.AbstractGenerator
import org.stevesea.rpgpad.data.RangeMap
import org.stevesea.rpgpad.data.Shuffler

@CompileStatic
class SWNWorld extends AbstractGenerator {

    SWNnames names = new SWNnames()
    SWNpolitical_party political = new SWNpolitical_party()

    @Override
    AbstractGenerator withShuffler(Shuffler shuff) {
        super.withShuffler(shuff)
        names.withShuffler(shuff)
        political.withShuffler(shuff)
        this
    }

    RangeMap atmospheres = new RangeMap()
            .with(2, 'Corrosive')
            .with(3, 'Inert gas')
            .with(4, 'Airless or thin atmosphere')
            .with(5..9, 'Breatheable mix')
            .with(10, 'Thick atmosphere, breathable with a pressure mask')
            .with(11, 'Invasive, toxic atmosphere')
            .with(12, 'Corrosive and invasive atmosphere')
    RangeMap temperatures = new RangeMap()
            .with(2, 'Frozen')
            .with(3, 'Variable cold-to-temperate')
            .with(4..5, 'Cold')
            .with(6..8, 'Temperate')
            .with(9..10, 'Warm')
            .with(11, 'Variable temperate-to-warm')
            .with(12, 'Burning')
    RangeMap biospheres = new RangeMap()
            .with(2, 'Biosphere remnants')
            .with(3, 'Microbial life')
            .with(4..5, 'No native biosphere')
            .with(6..8, 'Human-miscible biosphere')
            .with(9..10, 'Immiscible biosphere')
            .with(11, 'Hybrid biosphere')
            .with(12, 'Engineered biosphere')

    RangeMap populations = new RangeMap()
            .with(2, 'Failed colony')
            .with(3, 'Outpost')
            .with(4..5, 'Tens of thousands of inhabitants')
            .with(6..8, 'Hundreds of thousands of inhabitants')
            .with(9..10, 'Millions of inhabitants')
            .with(11, 'Billions of inhabitants')
            .with(12, 'Alien civilization')
    RangeMap techlevels = new RangeMap()
            .with(2, 'Tech Level 0. Stone-age technology.')
            .with(3, 'Tech level 1. Medieval technology.')
            .with(4, 'Tech level 2. Nineteenth-century technology.')
            .with(5..6, 'Tech level 3. Twentieth-century technology.')
            .with(7..10, 'Tech level 4. Baseline postech.')
            .with(11, 'Tech level 4 with specialties or some surviving pretech.')
            .with(12, 'Tech level 5. Pretech, pre-Silence technology.')

    @Canonical
    public static class WorldTag {
        String flavor
        List<String> enemies
        List<String> friends
        List<String> complications
        List<String> things
        List<String> places
    }
    Map<String, WorldTag> tagMap = [
            'Abandoned Colony': new WorldTag(
                    flavor: '''The world once hosted a colony, whether human or otherwise, until some crisis or natural disaster drove the inhabitants away or killed them off . The
colony might have been mercantile in nature, an expedition to extract valuable local resources, or it might have been a reclusive cabal of zealots. The
remains of the colony are usually in ruins, and might still be dangerous from the aftermath of whatever destroyed it in the fi rst place.''',
                    enemies: 'Crazed survivors, Ruthless plunderers of the ruins, Automated defense system'.tokenize(','),
                    friends: 'Inquisitive stellar archaeologist, Heir to the colony’s property, Local wanting the place cleaned out'.tokenize(','),
                    complications: 'The local government wants the ruins to remain a secret, The locals claim ownership of it, The colony is crumbling and dangerous to navigate'.tokenize(','),
                    things: 'Long-lost property deeds, Relic stolen by the colonists when they left, Historical record of the colonization attempt'.tokenize(','),
                    places: 'Decaying habitation block, Vine-covered town square, Structure buried by an ancient landslide'.tokenize(','),
            ),
'Alien Ruins' : new WorldTag(
        flavor: '''The world has significant alien ruins present. The locals may or may not permit others to investigate the ruins, and may make it difficult to remove
any objects of value without substantial payment.''',
        enemies: 'Customs inspector, Worshipper of the ruins, Hidden alien survivor'.tokenize(','),
        friends: 'Curious scholar, Avaricious local resident, Interstellar smuggler'.tokenize(','),
        complications: 'Traps in the ruins, Remote location, Paranoid customs officials'.tokenize(','),
        things: 'Precious alien artifacts, Objects left with the remains of a prior unsuccessful expedition, Untranslated alien texts, Untouched hidden ruins'.tokenize(','),
        places: 'Undersea ruin, Orbital ruin, Perfectly preserved alien building, Alien mausoleum'.tokenize(',')
),
'Altered Humanity' : new WorldTag(
        flavor: '''The humans on this world are visibly and drastically diff erent from normal humanity. They may have additional limbs, new sensory organs, or other
significant changes. Were these from ancestral eugenic manipulation, or from environmental toxins?''',
        enemies: 'Biochauvinist local, Local experimenter, Mentally unstable mutant'.tokenize(','),
        friends: 'Local seeking a “cure”, Curious xenophiliac, Anthropological researcher'.tokenize(','),
        complications: 'Alteration is contagious, Alteration is necessary for long-term survival, Locals fear and mistrust non-local humans'.tokenize(','),
        things: 'Original pretech mutagenic equipment, Valuable biological byproduct from the mutants, “Cure” for the altered genes, Record of the original colonial genotypes'.tokenize(','),
        places: 'Abandoned eugenics laboratory, An environment requiring the mutation for survival, A sacred site where the first local was transformed'.tokenize(',')
),
'Area 51' : new WorldTag(
        flavor: '''The world’s government is fully aware of their local stellar neighbors, but the common populace has no idea about it- and the government means to
keep it that way. Trade with government offi cials in remote locations is possible, but any attempt to clue the commoners in on the truth will be met
with lethal reprisals.''',
        enemies: 'Suspicious government minder, Free merchant who likes his local monopoly, Local who wants a specimen for dissection'.tokenize(','),
        friends: 'Crusading off world investigator, Conspiracy-theorist local, Idealistic government reformer'.tokenize(','),
        complications: 'The government has a good reason to keep the truth concealed, The government ruthlessly oppresses the natives, The government is actually composed of off worlders'.tokenize(','),
        things: 'Elaborate spy devices, Memory erasure tech, Possessions of the last off worlder who decided to spread the truth'.tokenize(','),
        places: 'Desert airfi eld, Deep subterranean bunker, Hidden mountain valley'.tokenize(',')
),
'Badlands World' : new WorldTag(
        flavor: '''Whatever the ostensible climate and atmosphere type, something horrible happened to this world. Biological, chemical, or nanotechnical weaponry
has reduced it to a wretched hellscape''',
        enemies: 'Mutated badlands fauna, Desperate local, Badlands raider chief'.tokenize(','),
        friends: 'Native desperately wishing to escape the world, Scientist researching ecological repair methods, Ruin scavenger'.tokenize(','),
        complications: 'Radioactivity, Bioweapon traces, Broken terrain, Sudden local plague'.tokenize(','),
        things: 'Maltech research core, Functional pretech weaponry, An uncontaminated well'.tokenize(','),
        places: 'Untouched oasis, Ruined city, Salt flat'.tokenize(',')
),
'Bubble Cities' : new WorldTag(
        flavor: '''Whether due to a lack of atmosphere or an uninhabitable climate, the world’s cities exist within domes or pressurized buildings. In such sealed
environments, techniques of surveillance and control can grow baroque and extreme.''',
        enemies: 'Native dreading outsider contamination, Saboteur from another bubble city, Local offi cial hostile to outsider ignorance of laws'.tokenize(','),
        friends: 'Local rebel against the city officials, Maintenance chief in need of help, Surveyor seeking new building sites'.tokenize(','),
        complications: 'Bubble rupture, Failing atmosphere reprocessor, Native revolt against offi cials, All-seeing surveillance cameras'.tokenize(','),
        things: 'Pretech habitat technology, Valuable industrial products, Master key codes to a city’s security system'.tokenize(','),
        places: 'City power core, Surface of the bubble, Hydroponics complex, Warren-like hab block'.tokenize(',')
),
'Civil War' : new WorldTag(
        flavor: '''The world is currently torn between at least two opposing factions, all of which claim legitimacy. The war may be the result of a successful rebel
uprising against tyranny, or it might just be the result of schemers who plan to be the new masters once the revolution is complete.''',
        enemies: 'Faction commissar, Angry native, Conspiracy theorist who blames off worlders for the war, Deserter looking out for himself, Guerilla bandits'.tokenize(','),
        friends: 'Faction loyalist seeking aid, Native caught in the crossfi re, Off worlder seeking passage off the planet'.tokenize(','),
        complications: 'The front rolls over the group, Famine strikes, Bandit infestations'.tokenize(','),
        things: 'Ammo dump, Military cache, Treasure buried for after the war, Secret war plans'.tokenize(','),
        places: 'Battle front, Bombed-out town, Rear-area red light zone, Propaganda broadcast tower'.tokenize(',')
),
'Cold War' : new WorldTag(
        flavor: '''Two or more great powers control the planet, and they have a hostility to each other that’s just barely less than open warfare. The hostility might be
ideological in nature, or it might revolve around control of some local resource.''',
        enemies: 'Suspicious chief of intelligence, Native who thinks the outworlders are with the other side, Femme fatale'.tokenize(','),
        friends: 'Apolitical information broker, Spy for the other side, Unjustly accused innocent, “He’s a bastard, but he’s our bastard” official'.tokenize(','),
        complications: 'Police sweep, Low-level skirmishing, “Red scare”'.tokenize(','),
        things: 'List of traitors in government, secret military plans, Huge cache of weapons built up in preparation for war'.tokenize(','),
        places: 'Seedy bar in a neutral area, Political rally, Isolated area where fighting is underway'.tokenize(',')
),
'Colonized Population' : new WorldTag(
        flavor: '''A neighboring world has successfully colonized this less-advanced or less-organized planet, and the natives aren’t happy about it. A puppet government
may exist, but all real decisions are made by the local viceroy.''',
        enemies: 'Suspicious security personnel, Off worlder-hating natives, Local crime boss preying on rich off worlders'.tokenize(','),
        friends: 'Native resistance leader, Colonial offi cial seeking help, Native caught between the two sides'.tokenize(','),
        complications: 'Natives won’t talk to off worlders, Colonial repression, Misunderstood local customs'.tokenize(','),
        things: 'Relic of the resistance movement, List of collaborators, Precious substance extracted by colonial labor'.tokenize(','),
        places: 'Deep wilderness resistance camp, City district off-limits to natives, Colonial labor site'.tokenize(',')
),
'Desert World' : new WorldTag(
        flavor: '''The world may have a breathable atmosphere and a human-tolerable temperature range, but it is an arid, stony waste outside of a few places made
habitable by human eff ort. The deep wastes are largely unexplored and inhabited by outcasts and worse.''',
        enemies: 'Raider chieftain, Crazed hermit, Angry isolationists, Paranoid mineral prospector, Strange desert beast'.tokenize(','),
        friends: 'Native guide, Research biologist, Aspiring terraformer'.tokenize(','),
        complications: 'Sandstorms, Water supply failure, Native warfare over water rights'.tokenize(','),
        things: 'Enormous water reservoir, Map of hidden wells, Pretech rainmaking equipment'.tokenize(','),
        places: 'Oasis, “The Empty Quarter” of the desert, Hidden underground cistern'.tokenize(',')
),
'Eugenic Cult' : new WorldTag(
        flavor: '''Even in the days before the Silence, major improvement of the human genome always seemed to come with unacceptable side-eff ects. Some worlds
host secret cults that perpetuate these improvements regardless of the cost, and a few planets have been taken over entirely by the cults.''',
        enemies: 'Eugenic superiority fanatic, Mentally unstable homo superior, Mad eugenic scientist'.tokenize(','),
        friends: 'Eugenic propagandist, Biotechnical investigator, Local seeking revenge on cult'.tokenize(','),
        complications: 'The altered cultists look human, The locals are terrifi ed of any unusual physical appearance, The genetic modifi cationsand drawbacks- are contagious with long exposure'.tokenize(','),
        things: 'Serum that induces the alteration, Elixir that reverses the alteration, Pretech biotechnical databanks, List of secret cult sympathizers'.tokenize(','),
        places: 'Eugenic breeding pit, Isolated settlement of altered humans, Public place infi ltrated by cult sympathizers'.tokenize(',')
),
'Exchange Consulate' : new WorldTag(
        flavor: '''The Exchange of Light once served as the largest, most trusted banking and diplomatic service in human space. Even after the Silence, some worlds
retain a functioning Exchange Consulate where banking services and arbitration can be arranged.''',
        enemies: 'Corrupt Exchange offi cial, Indebted native who thinks the players are Exchange agents, Exchange offi cial dunning the players for debts incurred'.tokenize(','),
        friends: 'Consul in need of off world help, Local banker seeking to hurt his competition, Exchange diplomat'.tokenize(','),
        complications: 'The local Consulate has been corrupted, the Consulate is cut off from its funds, A powerful debtor refuses to pay'.tokenize(','),
        things: 'Exchange vault codes, Wealth hidden to conceal it from a bankruptcy judgment, Location of forgotten vault'.tokenize(','),
        places: 'Consulate meeting chamber, Meeting site between fractious disputants, Exchange vault'.tokenize(',')
),
'Feral World' : new WorldTag(
        flavor: '''In the long, isolated night of the Silence, some worlds have experienced total moral and cultural collapse. Whatever remains has been twisted beyond
recognition into assorted death cults, xenophobic fanaticism, horrific cultural practices, or other behavior unacceptable on more enlightened worlds.
These worlds are almost invariably classed under Red trade codes.''',
        enemies: 'Decadent noble, Mad cultist, Xenophobic local, Cannibal chief, Maltech researcher'.tokenize(','),
        friends: 'Trapped outworlder, Aspiring reformer, Native wanting to avoid traditional flensing'.tokenize(','),
        complications: 'Horrifi c local “celebration”, Inexplicable and repugnant social rules, Taboo zones and people'.tokenize(','),
        things: 'Terribly misused piece of pretech, Wealth accumulated through brutal evildoing, Valuable possession owned by luckless outworlder victim'.tokenize(','),
        places: 'Atrocity amphitheater, Traditional torture parlor, Ordinary location twisted into something terrible.'.tokenize(',')
),
'Flying Cities' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Forbidden Tech' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Freak Geology' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Freak Weather' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Friendly Foe' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Gold Rush' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Hatred' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Heavy Industry' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Heavy Mining' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Hostile Biosphere' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Hostile Space' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Local Specialty' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Local Tech' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Major Spaceyard' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Minimal Contact' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Misandry/Misogyny' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Oceanic World' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Out of Contact' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Outpost World' : new WorldTag(
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Perimeter Agency' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Pilgrimage Site' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Police State' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Preceptor Archive' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Pretech Cultists' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Primitive Aliens' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Psionics Fear' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Psionics Worship' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Psionics Academy' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Quarantined World' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Radioactive World' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Regional Hegemon' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Restrictive Laws' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Rigid Culture' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Seagoing Cities' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Sealed Menace' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Sectarians' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Seismic Instability' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Secret Masters' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Theocracy' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Tomb World' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Trade Hub' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Tyranny' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Unbraked AI' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Warlords' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Xenophiles' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Xenophobes' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Zombies' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
    ]
    List<String> world_tags = """\
Abandoned Colony
Alien Ruins
Altered Humanity
Area 51
Badlands World
Bubble Cities
Civil War
Cold War
Colonized Population
Desert World
Eugenic Cult
Exchange Consulate
Feral World
Flying Cities
Forbidden Tech
Freak Geology
Freak Weather
Friendly Foe
Gold Rush
Hatred
Heavy Industry
Heavy Mining
Hostile Biosphere
Hostile Space
Local Specialty
Local Tech
Major Spaceyard
Minimal Contact
Misandry/Misogyny
Oceanic World
Out of Contact
Outpost World
Perimeter Agency
Pilgrimage Site
Police State
Preceptor Archive
Pretech Cultists
Primitive Aliens
Psionics Fear
Psionics Worship
Psionics Academy
Quarantined World
Radioactive World
Regional Hegemon
Restrictive Laws
Rigid Culture
Seagoing Cities
Sealed Menace
Sectarians
Seismic Instability
Secret Masters
Theocracy
Tomb World
Trade Hub
Tyranny
Unbraked AI
Warlords
Xenophiles
Xenophobes
Zombies
Artificial World
Weak Gravity
Heavy Gravity
""".readLines()


    String template =  '''\
<strong>World</strong> - {{name}}
<br/>
<br/><strong><small>Atmosphere:</small></strong> {{atmosphere}}
<br/><strong><small>Temperature:</small></strong> {{temperature}}
<br/><strong><small>Biosphere:</small></strong> {{biosphere}}
<br/>
<br/><strong><small>Population:</small></strong> {{population}}
<br/><strong><small>Tech Level:</small></strong> {{techlevel}}
<br/>
<br/><strong><small>World Tags:</small></strong>
<br/>&nbsp;&nbsp;{{tags}}
<br/>
<br/><strong><small>Cultures:</small></strong> {{cultures}}
'''
    String generate() {
        // how many cultures are on the world?
        int numCultures = pick(new RangeMap()
                .with(1..3, 1)
                .with(4..5, 2)
                .with(6, 3)
        ) as int

        List<String> cultures = names.pickCultures(numCultures)
        // pick a name from the 1st culture in list
        String worldName = names.getPlaceName(cultures[0])

        List<String> worldTags = pickN(world_tags, 2) as List<String>

        Mustache.compiler().compile(template).execute(
                [
                        name: worldName,
                        cultures: cultures.join(', '),
                        atmosphere: pick('2d6', atmospheres),
                        temperature: pick('2d6', temperatures),
                        biosphere: pick('2d6', biospheres),
                        population: pick('2d6', populations),
                        techlevel: pick('2d6', techlevels),
                        tags: worldTags.join('<br/>&nbsp;&nbsp; '),
                ]
        )
    }
}
