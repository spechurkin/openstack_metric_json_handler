package proj.work.consoleCreation.compute

import org.openstack4j.api.Builders

fun main(args: Array<String>) {
    createFlavor(args)
//    removeFlavor(getFlavorIdByName("DefaultFlavor"))
}

fun createFlavor(args: Array<String>) {
    val flavorCreate = Builders.flavor()
        .name(args.getOrElse(0) { "DefaultFlavor" })
        .ram(args.getOrElse(1) { "1024" }.toInt())
        .disk(args.getOrElse(2) { "5" }.toInt())
        .ephemeral(args.getOrElse(3) { "5" }.toInt())
        .vcpus(args.getOrElse(4) { "2" }.toInt())
        .build()

    os.compute().flavors().create(flavorCreate)
}

fun removeFlavor(flavorId: String?) {
    os.compute().flavors().delete(flavorId)
}

fun getFlavorIdByName(flavorName: String): String? {
    return os.compute().flavors().list().find { it.name == flavorName }?.id
}