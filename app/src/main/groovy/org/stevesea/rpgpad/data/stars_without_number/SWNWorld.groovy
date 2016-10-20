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
        flavor: '''Perhaps the world is a gas giant, or plagued with unendurable storms at lower levels of the atmosphere. For whatever reason, the cities of this world
fl y above the surface of the planet. Perhaps they remain stationary, or perhaps they move from point to point in search of resources.''',
        enemies: 'Rival city pilot, Tech thief attempting to steal outworld gear, Saboteur or scavenger plundering the city’s tech'.tokenize(','),
        friends: 'Maintenance tech in need of help, City defense force pilot, Meteorological researcher'.tokenize(','),
        complications: 'Sudden storms, Drastic altitude loss, Rival city attacks, Vital machinery breaks down'.tokenize(','),
        things: 'Precious refi ned atmospheric gases, Pretech grav engine plans, Meteorological codex predicting future storms'.tokenize(','),
        places: 'Underside of the city, Th e one calm place on the planet’s surface, Catwalks stretching over unimaginable gulfs below.'.tokenize(',')
),
'Forbidden Tech' : new WorldTag(
        flavor: '''Some group on this planet fabricates or uses maltech. Unbraked AIs doomed to metastasize into insanity, nation-destroying nanowarfare particles,
slow-burn DNA corruptives, genetically engineered slaves, or something worse still. Th e planet’s larger population may or may not be aware of the
danger in their midst.''',
        enemies: 'Mad scientist, Maltech buyer from off world, Security enforcer'.tokenize(','),
        friends: 'Victim of maltech, Perimeter agent, Investigative reporter, Conventional arms merchant'.tokenize(','),
        complications: 'Th e maltech is being fabricated by an unbraked AI, Th e government depends on revenue from maltech sales to off worlders, Citizens insist that it’s not really maltech'.tokenize(','),
        things: 'Maltech research data, Th e maltech itself, Precious pretech equipment used to create it'.tokenize(','),
        places: 'Horrific laboratory, Hellscape sculpted by the maltech’s use, Government building meeting room'.tokenize(',')
),

            'Friendly Foe' : new WorldTag(
                    flavor: '''Some hostile alien race or malevolent cabal has a branch or sect on this world that is actually quite friendly toward outsiders. For whatever internal
reason, they are willing to negotiate and deal honestly with strangers, and appear to lack the worst impulses of their fellows.''',
                    enemies: 'Driven hater of all their kind, Internal malcontent bent on creating confl ict, Secret master who seeks to lure trust'.tokenize(','),
                    friends: 'Well-meaning bug-eyed monster, Principled eugenics cultist, Suspicious investigator'.tokenize(','),
                    complications: 'Th e group actually is as harmless and benevolent as they seem, Th e group off ers a vital service at the cost of moral compromise, Th e group still feels bonds of affi liation with their hostile brethren'.tokenize(','),
                    things: 'Forbidden xenotech, Eugenic biotech template, Evidence to convince others of their kind that they are right'.tokenize(','),
                    places: 'Repurposed maltech laboratory, Alien conclave building, Widely-feared starship interior'.tokenize(',')
            ),
'Freak Geology' : new WorldTag(
        flavor: '''Th e geology or geography of this world is simply freakish. Perhaps it’s composed entirely of enormous mountain ranges, or regular bands of land
and sea, or the mineral structures all fragment into perfect cubes. Th e locals have learned to deal with it and their culture will be shaped by its
requirements.''',
        enemies: 'Crank xenogeologist, Cultist who believes it the work of aliens'.tokenize(','),
        friends: 'Research scientist, Prospector, Artist'.tokenize(','),
        complications: 'Local conditions that no one remembers to tell outworlders about, Lethal weather, Seismic activity'.tokenize(','),
        things: 'Unique crystal formations, Hidden veins of a major precious mineral strike, Deed to a location of great natural beauty'.tokenize(','),
        places: 'Atop a bizarre geological formation, Tourist resort catering to off worlders'.tokenize(',')
),
'Freak Weather' : new WorldTag(
        flavor: '''Th e planet is plagued with some sort of bizarre or hazardous weather pattern. Perhaps city-fl attening storms regularly scourge the surface, or the
world’s sun never pierces its thick banks of clouds.''',
        enemies: 'Criminal using the weather as a cover, Weather cultists convinced the off worlders are responsible for some disaster, Native predators dependent on the weather'.tokenize(','),
        friends: 'Meteorological researcher, Holodoc crew wanting shots of the weather'.tokenize(','),
        complications: 'Th e weather itself, Malfunctioning pretech terraforming engines that cause the weather'.tokenize(','),
        things: 'Wind-scoured deposits of precious minerals, Holorecords of a spectacularly and rare weather pattern, Naturallysculpted objects of intricate beauty'.tokenize(','),
        places: 'Eye of the storm, Th e one sunlit place, Terraforming control room'.tokenize(',')
),
'Gold Rush' : new WorldTag(
        flavor: '''Gold, silver, and other conventional precious minerals are common and cheap now that asteroid mining is practical for most worlds. But some
minerals and compounds remain precious and rare, and this world has recently been discovered to have a supply of them. People from across the
sector have come to strike it rich.''',
        enemies: 'Paranoid prospector, Aspiring mining tycoon, Rapacious merchant'.tokenize(','),
        friends: 'Claim-jumped miner, Native alien, Curious tourist'.tokenize(','),
        complications: 'The strike is a hoax, The strike is of a dangerous toxic substance, Export of the mineral is prohibited by the planetary government, Th e native aliens live around the strike’s location'.tokenize(','),
        things: 'Cases of the refi ned element, Pretech mining equipment, A dead prospector’s claim deed'.tokenize(','),
        places: 'Secret mine, Native alien village, Processing plant, Boom town'.tokenize(',')
),
'Hatred' : new WorldTag(
        flavor: '''For whatever reason, this world’s populace has a burning hatred for the inhabitants of a neighboring system. Perhaps this world was colonized by
exiles, or there was a recent interstellar war, or ideas of racial or religious superiority have fanned the hatred. Regardless of the cause, the locals view
their neighbor and any sympathizers with loathing.''',
        enemies: 'Native convinced that the off worlders are agents of Th em, Cynical politician in need of scapegoats'.tokenize(','),
        friends: 'Intelligence agent needing catspaws, Holodoc producers needing “an inside look”'.tokenize(','),
        complications: 'Th e characters are wearing or using items from the hated world, Th e characters are known to have done business there, Th e characters “look like” the hated others'.tokenize(','),
        things: 'Proof of Th eir evildoing, Reward for turning in enemy agents, Relic stolen by Th em years ago'.tokenize(','),
        places: 'War crimes museum, Atrocity site, Captured, decommissioned spaceship kept as a trophy'.tokenize(',')
),
'Heavy Industry' : new WorldTag(
        flavor: '''With interstellar transport so limited in the bulk it can move, worlds have to be largely self-suffi cient in industry. Some worlds are more suffi cient
than others, however, and this planet has a thriving manufacturing sector capable of producing large amounts of goods appropriate to its tech level.
Th e locals may enjoy a correspondingly higher lifestyle, or the products might be devoted towards vast projects for the aggrandizement of the rulers.''',
        enemies: 'Tycoon monopolist, Industrial spy, Malcontent revolutionary'.tokenize(','),
        friends: 'Aspiring entrepreneur, Worker union leader, Ambitious inventor'.tokenize(','),
        complications: 'Th e factories are toxic, Th e resources extractable at their tech level are running out, Th e masses require the factory output for survival, Th e industries’ major output is being obsoleted by off world tech'.tokenize(','),
        things: 'Confidential industrial data, Secret union membership lists, Ownership shares in an industrial complex'.tokenize(','),
        places: 'Factory floor, Union meeting hall, Toxic waste dump, R&D complex'.tokenize(',')
),
'Heavy Mining' : new WorldTag(
        flavor: '''Th is world has large stocks of valuable minerals, usually necessary for local industry, life support, or refi nement into loads small enough to export
off world. Major mining eff orts are necessary to extract the minerals, and many natives work in the industry.''',
        enemies: 'Mine boss, Tunnel saboteur, Subterranean predators'.tokenize(','),
        friends: 'Hermit prospector, Off world investor, Miner’s union representative'.tokenize(','),
        complications: 'Th e refi nery equipment breaks down, Tunnel collapse, Silicate life forms growing in the miners’ lungs'.tokenize(','),
        things: 'Th e mother lode, Smuggled case of refi ned mineral, Faked crystalline mineral samples'.tokenize(','),
        places: 'Vertical mine face, Tailing piles, Roaring smelting complex'.tokenize(',')
),
'Hostile Biosphere' : new WorldTag(
        flavor: '''Th e world is teeming with life, and it hates humans. Perhaps the life is xenoallergenic, forcing fi lter masks and tailored antiallergens for survival. It
could be the native predators are huge and fearless, or the toxic fl ora ruthlessly outcompetes earth crops.''',
        enemies: 'Local fauna, Nature cultist, Native aliens, Callous labor overseer'.tokenize(','),
        friends: 'Xenobiologist, Tourist on safari, Grizzled local guide'.tokenize(','),
        complications: 'Filter masks fail, Parasitic alien infestation, Crop greenhouses lose bio-integrity'.tokenize(','),
        things: 'Valuable native biological extract, Abandoned colony vault, Remains of an unsuccessful expedition'.tokenize(','),
        places: 'Deceptively peaceful glade, Steaming polychrome jungle, Nightfall when surrounded by Th ings'.tokenize(',')
),
'Hostile Space' : new WorldTag(
        flavor: '''Th e system in which the world exists is a dangerous neighborhood. Something about the system is perilous to inhabitants, either through meteor
swarms, stellar radiation, hostile aliens in the asteroid belt, or periodic comet clouds''',
        enemies: 'Alien raid leader, Meteor-launching terrorists, Paranoid local leader'.tokenize(','),
        friends: 'Astronomic researcher, Local defense commander, Early warning monitor agent'.tokenize(','),
        complications: 'Th e natives believe the danger is divine chastisement, Th e natives blame outworlders for the danger, Th e native elite profi t from the danger in some way'.tokenize(','),
        things: 'Early warning of a raid or impact, Abandoned riches in a disaster zone, Key to a secure bunker'.tokenize(','),
        places: 'City watching an approaching asteroid, Village burnt in an alien raid, Massive ancient crater'.tokenize(',')
),
'Local Specialty' : new WorldTag(
        flavor: '''Th e world may be sophisticated or barely capable of steam engines, but either way it produces something rare and precious to the wider galaxy. It
might be some pharmaceutical extract produced by a secret recipe, a remarkably popular cultural product, or even gengineered humans uniquely
suited for certain work.''',
        enemies: 'Monopolist, Off worlder seeking prohibition of the specialty, Native who views the specialty as sacred'.tokenize(','),
        friends: 'Spy searching for the source, Artisan seeking protection, Exporter with problems'.tokenize(','),
        complications: 'Th e specialty is repugnant in nature, Th e crafters refuse to sell to off worlders, Th e specialty is made in a remote, dangerous place, Th e crafters don’t want to make the specialty any more'.tokenize(','),
        things: 'The specialty itself, Th e secret recipe, Sample of a new improved variety'.tokenize(','),
        places: 'Secret manufactory, Hidden cache, Artistic competition for best artisan'.tokenize(',')
),
'Local Tech' : new WorldTag(
        flavor: '''Th e locals can create a particular example of extremely high tech, possibly even something that exceeds pretech standards. Th ey may use unique local
resources to do so, or have stumbled on a narrow scientifi c breakthrough, or still have a functional experimental manufactory.''',
        enemies: 'Keeper of the tech, Off world industrialist, Automated defenses that suddenly come alive, Native alien mentors'.tokenize(','),
        friends: 'Curious off world scientist, Eager tech buyer, Native in need of technical help'.tokenize(','),
        complications: 'Th e tech is unreliable, Th e tech only works on this world, Th e tech has poorly-understood side eff ects, Th e tech is alien in nature.'.tokenize(','),
        things: 'Th e tech itself, An unclaimed payment for a large shipment, Th e secret blueprints for its construction, An ancient alien R&D database'.tokenize(','),
        places: 'Alien factory, Lethal R&D center, Tech brokerage vault'.tokenize(',')
),
'Major Spaceyard' : new WorldTag(
        flavor: '''Most worlds of tech level 4 or greater have the necessary tech and orbital facilities to build spike drives and starships. Th is world is blessed with a
major spaceyard facility, either inherited from before the Silence or painstakingly constructed in more recent decades. It can build even capital-class
hulls, and do so more quickly and cheaply than its neighbors''',
        enemies: 'Enemy saboteur, Industrial spy, Scheming construction tycoon, Aspiring ship hijacker'.tokenize(','),
        friends: 'Captain stuck in drydock, Maintenance chief, Mad innovator'.tokenize(','),
        complications: 'Th e spaceyard is an alien relic, Th e spaceyard is burning out from overuse, Th e spaceyard is alive, Th e spaceyard relies on maltech to function'.tokenize(','),
        things: 'Intellectual property-locked pretech blueprints, Override keys for activating old pretech facilities, A purchased but unclaimed spaceship.'.tokenize(','),
        places: 'Hidden shipyard bay, Surface of a partially-completed ship, Ship scrap graveyard'.tokenize(',')
),
'Minimal Contact' : new WorldTag(
        flavor: '''Th e locals refuse most contact with off worlders. Only a small, quarantined treaty port is provided for off world trade, and ships can expect an
exhaustive search for contraband. Local governments may be trying to keep the very existence of interstellar trade a secret from their populations, or
they may simply consider off worlders too dangerous or repugnant to be allowed among the population.''',
        enemies: 'Customs offi cial, Xenophobic natives, Existing merchant who doesn’t like competition'.tokenize(','),
        friends: 'Aspiring tourist, Anthropological researcher, Off world thief, Religious missionary'.tokenize(','),
        complications: 'Th e locals carry a disease harmless to them and lethal to outsiders, Th e locals hide dark purposes from off worlders, Th e locals have something desperately needed but won’t bring it into the treaty port'.tokenize(','),
        things: 'Contraband trade goods, Security perimeter codes, Black market local products'.tokenize(','),
        places: 'Treaty port bar, Black market zone, Secret smuggler landing site'.tokenize(',')
),
'Misandry/Misogyny' : new WorldTag(
        flavor: '''Th e culture on this world holds a particular gender in contempt. Members of that gender are not permitted positions of formal power, and may
be restricted in their movements and activities. Some worlds may go so far as to scorn both traditional genders, using gengineering techniques to
hybridize or alter conventional human biology''',
        enemies: 'Cultural fundamentalist, Cultural missionary to outworlders'.tokenize(','),
        friends: 'Oppressed native, Research scientist, Off world emancipationist, Local reformer'.tokenize(','),
        complications: 'Th e oppressed gender is restive against the customs, Th e oppressed gender largely supports the customs, Th e customs relate to some physical quality of the world, Th e oppressed gender has had maltech gengineering done to “tame” them.'.tokenize(','),
        things: 'Aerosol reversion formula for undoing gengineered docility, Hidden history of the world, Pretech gengineering equipment'.tokenize(','),
        places: 'Shrine to the virtues of the favored gender, Security center for controlling the oppressed, Gengineering lab'.tokenize(',')
),
'Oceanic World' : new WorldTag(
        flavor: '''Th e world is entirely or almost entirely covered with liquid water. Habitations might be fl oating cities, or might cling precariously to the few rocky
atolls jutting up from the waves, or are planted as bubbles on promontories deep beneath the stormy surface. Survival depends on aquaculture.
Planets with inedible alien life rely on gengineered Terran sea crops.''',
        enemies: 'Pirate raider, Violent “salvager” gang, Tentacled sea monster'.tokenize(','),
        friends: 'Daredevil fi sherman, Sea hermit, Sapient native life'.tokenize(','),
        complications: 'Th e liquid fl ux confuses grav engines too badly for them to function on this world, Sea is corrosive or toxic, Th e seas are wracked by regular storms'.tokenize(','),
        things: 'Buried pirate treasure, Location of enormous schools of fi sh, Pretech water purifi cation equipment'.tokenize(','),
        places: 'Th e only island on the planet, Floating spaceport, Deck of a storm-swept ship, Undersea bubble city'.tokenize(',')
),
'Out of Contact' : new WorldTag(
        flavor: '''Th e natives have been entirely out of contact with the greater galaxy for centuries or longer. Perhaps the original colonists were seeking to hide from
the rest of the universe, or the Silence destroyed any means of communication. It may have been so long that human origins on other worlds have
regressed into a topic for legends. Th e players might be on the fi rst off world ship to land since the First Wave of colonization a thousand years ago.''',
        enemies: 'Fearful local ruler, Zealous native cleric, Sinister power that has kept the world isolated'.tokenize(','),
        friends: 'Scheming native noble, Heretical theologian, UFO cultist native'.tokenize(','),
        complications: 'Automatic defenses fi re on ships that try to take off , Th e natives want to stay out of contact, Th e natives are highly vulnerable to off world diseases, Th e native language is completely unlike any known to the group'.tokenize(','),
        things: 'Ancient pretech equipment, Terran relic brought from Earth, Logs of the original colonists'.tokenize(','),
        places: 'Long-lost colonial landing site, Court of the local ruler, Ancient defense battery controls'.tokenize(',')
),
'Outpost World' : new WorldTag(
        flavor: '''Th e world is only a tiny outpost of human habitation planted by an off world corporation or government. Perhaps the staff is there to serve as a
refueling and repair stop for passing ships, or to oversee an automated mining and refi nery complex. Th ey might be there to study ancient ruins, or
simply serve as a listening and monitoring post for traffi c through the system. Th e outpost is likely well-equipped with defenses against casual piracy.''',
        enemies: 'Space-mad outpost staff er, Outpost commander who wants it to stay undiscovered, Undercover saboteur'.tokenize(','),
        friends: 'Lonely staff er, Fixated researcher, Overtaxed maintenance chief'.tokenize(','),
        complications: 'Th e alien ruin defense systems are waking up, Atmospheric disturbances trap the group inside the outpost for a month, Pirates raid the outpost, Th e crew have become converts to a strange set of beliefs'.tokenize(','),
        things: 'Alien relics, Vital scientifi c data, Secret corporate exploitation plans'.tokenize(','),
        places: 'Grimy recreation room, Refueling station, Th e only building on the planet, A “starport” of swept bare rock.'.tokenize(',')
),
'Perimeter Agency' : new WorldTag(
        flavor: '''Before the Silence, the Perimeter was a Terran-sponsored organization charged with rooting out use of maltech- technology banned in human space
as too dangerous for use or experimentation. Unbraked AIs, gengineered slave species, nanotech replicators, weapons of planetary destruction... the
Perimeter hunted down experimenters with a great indiff erence to planetary laws. Most Perimeter Agencies collapsed during the Silence, but a few
managed to hold on to their mission, though modern Perimeter agents often fi nd more work as conventional spies and intelligence operatives.''',
        enemies: 'Renegade Agency Director, Maltech researcher, Paranoid intelligence chief'.tokenize(','),
        friends: 'Agent in need of help, Support staff er, “Unjustly” targeted researcher'.tokenize(','),
        complications: 'Th e local Agency has gone rogue and now uses maltech, Th e Agency archives have been compromised, Th e Agency has been targeted by a maltech-using organization, Th e Agency’s existence is unknown to the locals'.tokenize(','),
        things: 'Agency maltech research archives, Agency pretech spec-ops gear, File of blackmail on local politicians'.tokenize(','),
        places: 'Interrogation room, Smoky bar, Maltech laboratory, Secret Agency base'.tokenize(',')
),
'Pilgrimage Site' : new WorldTag(
        flavor: '''Th e world is noted for an important spiritual or historical location, and might be the sector headquarters for a widespread religion or political
movement. Th e site attracts wealthy pilgrims from throughout nearby space, and those with the money necessary to manage interstellar travel can
be quite generous to the site and its keepers. Th e locals tend to be fi ercely protective of the place and its reputation, and some places may forbid the
entrance of those not suitably pious or devout.''',
        enemies: 'Saboteur devoted to a rival belief, Bitter reformer who resents the current leadership, Swindler conning the pilgrims'.tokenize(','),
        friends: 'Protector of the holy site, Naive off worlder pilgrim, Outsider wanting to learn the sanctum’s inner secrets'.tokenize(','),
        complications: 'Th e site is actually a fake, Th e site is run by corrupt and venal keepers, A natural disaster threatens the site'.tokenize(','),
        things: 'Ancient relic guarded at the site, Proof of the site’s inauthenticity, Precious off ering from a pilgrim'.tokenize(','),
        places: 'Incense-scented sanctum, Teeming crowd of pilgrims, Imposing holy structure'.tokenize(',')
),
'Police State' : new WorldTag(
        flavor: '''Th e world is a totalitarian police state. Any sign of disloyalty to the planet’s rulers is punished severely, and suspicion riddles society. Some worlds
might operate by Soviet-style informers and indoctrination, while more technically sophisticated worlds might rely on omnipresent cameras or braked
AI “guardian angels”. Outworlders are apt to be treated as a necessary evil at best, and “disappeared” if they become troublesome''',
        enemies: 'Secret police chief, Scapegoating offi cial, Treacherous native informer'.tokenize(','),
        friends: 'Rebel leader, Off world agitator, Imprisoned victim, Crime boss'.tokenize(','),
        complications: 'Th e natives largely believe in the righteousness of the state, Th e police state is automated and its “rulers” can’t shut it off , Th e leaders foment a pogrom against “off worlder spies”.'.tokenize(','),
        things: 'List of police informers, Wealth taken from “enemies of the state”, Dear Leader’s private stash'.tokenize(','),
        places: 'Military parade, Gulag, Gray concrete housing block, Surveillance center'.tokenize(',')
),
'Preceptor Archive' : new WorldTag(
        flavor: '''Th e Preceptors of the Great Archive were a pre-Silence organization devoted to ensuring the dissemination of human culture, history, and basic
technology to frontier worlds that risked losing this information during the human expansion. Most frontier planets had an Archive where natives
could learn useful technical skills in addition to human history and art. Th ose Archives that managed to survive the Silence now strive to send their
missionaries of knowledge to new worlds in need of their lore.''',
        enemies: 'Luddite native, Off world Merchant who wants the natives kept ignorant, Religious zealot, Corrupted First Speaker who wants to keep a monopoly on learning'.tokenize(','),
        friends: 'Preceptor Adept missionary, Off world scholar, Reluctant student, Roving Preceptor Adept'.tokenize(','),
        complications: 'Th e local Archive has taken a very religious and mystical attitude toward their teaching, Th e Archive has maintained some replicable pretech science, Th e Archive has been corrupted and their teaching is incorrect'.tokenize(','),
        things: 'Lost Archive database, Ancient pretech teaching equipment, Hidden cache of theologically unacceptable tech'.tokenize(','),
        places: 'Archive lecture hall, Experimental laboratory, Student-local riot'.tokenize(',')
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
