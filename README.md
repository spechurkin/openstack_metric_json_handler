<img data-rme-type="html" alt="NN2 Logo" height="247" width="421" src="nn2.png" />

# Opensearch Plugin для OpenStack

## Установка и настройка

### Настройка окружения

Для корректной работы плагина, необходимо внести изменения в `java.policy`.
В зависимости от того, как установлена и, соответственно, какая java машина используется, `java.policy` может
располагаться в различных местах.

Как правило, в инсталляциях SmartMonitor, используется `jvm`, поставляемая с OpenSearch. В этом случае файл будет
расположен в `{корневая_директория_opensearch}/jdk/conf/security`.

В самом файле, помимо настроек для работы SmartMonitor, необходимо добавить:

```
    permission java.net.NetPermission "getProxySelector";
    permission java.lang.RuntimePermission "getClassLoader";
    permission java.lang.RuntimePermission "setContextClassLoader";
    permission java.io.FilePermission "/home/opensearch/", "read";
    permission java.io.FilePermission "/home/opensearch/.kube/config", "read";
    permission java.io.FilePermission "/app/opensearch/jdk/lib/security/jssecacerts", "read";
    permission java.io.FilePermission "/app/opensearch/jdk/lib/security/cacerts", "read";
    permission java.io.FilePermission "/app/opensearch/config/nn2/openstack_plugin.yml", "read";
```

### Настройка конфигурационных файлов

Для работы плагина, необходимо создать папку `nn2` в базовой конфигурационной директории (как правило,
папка config (`{корневая_директория_opensearch}/config/nn2/openstack_plugin.yml}`).
В папке `nn2` необходимо создать файл `openstack_plugin.yml` со следующим содержанием:

```yaml
openstack.authUrl: <>
openstack.user: <>
openstack.pass: <>
openstack.domain: <>
openstack.project: <>
openstack.insecure: <>
```

##### Описание параметров

> **Внимание!**
> Конфигурация приложения представляет собой YAML файл. Отступы крайне важны и влияют на распознавание содержимого
> файла.

- `openstack.authUrl` - ссылка на авторизацию в OpenStack. Обычно имеет форму `https://<URl>:5000/v3`

- `openstack.user` - логин пользователя, который будет взаимодействовать с системой

- `openstack.pass` - пароль пользователя

- `openstack.domain` - высокоуровневый домен проект — по умолчанию имеет название `default`

- `openstack.project` - название проекта для взаимодействия. По умолчанию создаётся `admin`

- `openstack.insecure` - поле зарезервировано на будущее, по умолчанию ставить `true`

### Установка плагина

Установка плагина выполняется стандартной командой `opensearch-install`. Пример:

```
{корневая_директория_opensearch}/bin/opensearch-plugin install file://{путь_до_зип_файла_плагина}
```

### Удаление плагина

Для полного удаления плагина, необходимо:

1. Удалить плагин с помощью стандартной командой `opensearch-install`. Пример:

```
{корневая_директория_opensearch}/bin/opensearch-plugin remove nn2openstackplugin
```

1. Удалить папку с конфигурацией плагина (`{корневая_директория_opensearch}/config/nn2/`).

### Обновление плагина

Для обновления плагина необходимо только переустановить сам плагин.

```
Удалять или менять содержимое папки с конфигурацией - не требуется.
```

Необходимо:

1. Удалить плагин с помощью стандартной командой `opensearch-install`. Пример:

```
{корневая_директория_opensearch}/bin/opensearch-plugin remove nn2openstackplugin
```

1. Установить плагин с помощью стандартной командой `opensearch-install`. Пример:

```
{корневая_директория_opensearch}/bin/opensearch-plugin install file://{путь_до_зип_файла_плагина}
```

1. Перезапустить OpenSearch

## Запуск OpenSearch с плагином

При запуске необходимо визуально убедиться в наличии следующих записей в логе:

```
loaded plugin [nn2openstackplugin]
```

## Доступные endpoints

Все запросы к эндпоинтам поддерживаются методом `GET`, `POST` (в разработке), `PUT` (в разработке), `DELETE`  (в
разработке).
Доступны следующие группы:

- [`Compute`](#раздел-compute)

- [`Block Storage`](#раздел-block-storage)

- [`Networking`](#раздел-networking)

- [`Identity`](#раздел-identity)

---

> **Внимание!**
> Строки имён, команд должны быть экранированы

# Раздел `Compute`

## Подраздел `Servers`

### _nn2/openstack/compute/servers

#### Получение списка серверов

_Пример запроса:_

```http request
GET _nn2/openstack/compute/servers
```

##### Возвращаемые данные

- `id` - id сервера

- `name` - название сервера

- `status` - статус работы/запуска

- `flavor` - тип сервера с характеристиками

- `addresses` - сети подключения

- `securityGroups` - группы безопасности

- `keyName` - ключевая пара

- `created` - дата создания

- `updated` - дата обновления

### _nn2/openstack/compute/servers/create

#### Создание сервера

_Пример запроса:_

```http request
POST /_nn2/openstack/compute/servers/create

{
    "adminPass": "admin",
    "flavorName": "DefaultFlavor",
    "imageName": "Ubuntu Server 24.10",
    "keyPair": "RenderManKey",
    "networkNames": "RENDERMAN_NETWORK",
    "serverName": "RenderMan"
}
```

---

## Подраздел `Images`

### _nn2/openstack/compute/images

#### Получение списка образов

_Пример запроса:_

```http request
GET _nn2/openstack/compute/images
```

##### Возвращаемые данные

- `id` - id образа

- `name` - название образа

- `minRam` - минимальный объём RAM для образа

- `minDisk` - минимальный размер диска для образа

- `size` - размер образа (в байтах)

- `status` - статус образа

- `created` - дата создания

- `updated` - дата обновления

#### Создание образа

_Пример запроса:_

```http request
POST /_nn2/openstack/compute/images/create

{
    "imageName": "Ubuntu Server 25.04",
    "minRamGb": 1,
    "minDiskGb": 5,
    "visibility": "Общий",
    "urlToImg": "https://cloud-images.ubuntu.com/releases/plucky/release/ubuntu-25.04-server-cloudimg-amd64.img"
}
```

---

## Подраздел `Flavors`

### _nn2/openstack/compute/flavors

#### Получение списка типов сервера

_Пример запроса:_

```http request
GET _nn2/openstack/compute/flavors
```

##### Возвращаемые данные

- `id` - id типа сервера

- `name` - название типа сервера

- `vcpu` - количество виртуальных процессоров

- `ram` - выделенный объём RAM

- `disk` - выделенный объём диска

- `ephemeral` - временный диск

- `swap` - диск подкачки

- `rxtx` - RX/TX соотношение

#### Создание типа сервера

_Пример запроса:_

```http request
POST /_nn2/openstack/compute/flavors/create

{
    "flavorName": "m1.big",
    "ramMb": 4096,
    "diskGb": 20,
    "ephemeralGb": 0,
    "swapMb": 512,
    "vcpus": 4,
    "rxtxFactor": 1.2,
    "isPublic": true
}
```

---

## Подраздел `Server Groups`

### _nn2/openstack/compute/serverGroups

#### Получение списка групп серверов

_Пример запроса:_

```http request
GET _nn2/openstack/compute/serverGroups
```

##### Возвращаемые данные

- `id` - id типа сервера

- `name` - название типа сервера

- `members` - названия серверов-участников группы

- `policies` - политика группы

---

## Подраздел `Keypair`

### _nn2/openstack/compute/keypairs

#### Получение списка ключевых пар

_Пример запроса:_

```http request
GET _nn2/openstack/compute/keypairs
```

##### Возвращаемые данные

- `id` – ID ключа

- `name` – название ключа

- `fingerprint` – отпечаток ключа

- `user` – пользователь, к которому привязан ключ

- `publicKey` – публичный ключа

- `privateKey` – приватный ключа

- `createdAt` – дата создания

- `updatedAt` – дата обновления

- `isDeleted` – удалён ли ключ?

- `deletedAt` – дата удаления

---

## Подраздел `Hypervisors`

### _nn2/openstack/compute/hypervisors

#### Получение данных гипервизора

_Пример запроса:_

```http request
GET _nn2/openstack/compute/hypervisors
```

##### Возвращаемые данные

- `id` – ID гипервизора

- `status` – статус гипервизора

- `state` – состояние гипервизора

- `cpuInfo` – информация о CPU

- `virtualCPU` – количество виртуальных CPU

- `virtualUsedCPU` – количество используемых виртуальных CPU

- `localDisk` – локальный диск (ГБ)

- `localDiskUsed` – используемый объём локального диска (ГБ)

- `freeDisk` – свободный объём диска (ГБ)

- `leastDiskAvailable` – минимально доступный объём диска (ГБ)

- `localMemory` – локальная память (МБ)

- `localMemoryUsed` – используемая локальная память (МБ)

- `freeRam` – свободная RAM (МБ)

- `hostIP` – IP-адрес хоста

- `hypervisorHostname` – имя хоста гипервизора

- `currentWorkload` – текущая нагрузка

- `runningVM` – количество запущенных ВМ

---

## Подраздел `Host Aggregates`

### _nn2/openstack/compute/hostAggregates

#### Получение списка агрегатов узлов

_Пример запроса:_

```http request
GET _nn2/openstack/compute/hostAggregates
```

##### Возвращаемые данные

- `id` – ID агрегата

- `name` – название агрегата

- `hosts` – список ID хостов

- `availabilityZone` – зона доступности

- `createdAt` – дата создания

- `updatedAt` – дата обновления

- `idDeleted` – удалён ли?

---

## Подраздел `Zones`

### _nn2/openstack/compute/zones

#### Получение списка зон доступа

_Пример запроса:_

```http request
GET _nn2/openstack/compute/zones
```

##### Возвращаемые данные

- `zoneName` – название зоны

- `isAvailable` – доступность зоны

- `hosts` – список хостов и сервисов

---

## Подраздел `Migrations`

### _nn2/openstack/compute/migrations

#### Получение списка миграций

_Пример запроса:_

```http request
GET _nn2/openstack/compute/migrations
```

##### Возвращаемые данные

- `id` – ID миграции

- `status` – статус миграции

- `sourceNode` – исходный узел

- `destNode` – целевой узел

- `sourceCompute` – исходный сервер

- `destCompute` – целевой сервер

- `destHost` – целевой хост

- `createdAt` – дата создания

- `updatedAt` – дата обновления

---

# Раздел `Block Storage`

## Подраздел `Volumes`

### _nn2/openstack/blockStorage/volumes

#### Получение списка дисков

_Пример запроса:_

```http request
GET _nn2/openstack/blockStorage/volumes
```

##### Возвращаемые данные

- `id` - id диска

- `name` - название диска

- `description` - описание диска

- `size` - размер диска (в Гб)

- `status` - статус работы диска

- `metadata` - метадата диска

- `created` - дата создания

### _nn2/openstack/blockStorage/volumes/create

#### Создание диска

_Пример запроса:_

```http request
POST _nn2/openstack/blockStorage/volumes/create

{
    "volumeName": "Ubuntu Server Volume",
    "description": "Bootable Ubuntu Volume",
    "size": 20,
    "volumeType": "high-iops",
    "bootable": true,
    "imageRef": "Ubuntu Server 25.04"
}
```

---

## Подраздел `Volume Types`

### _nn2/openstack/blockStorage/volumeTypes

#### Получение списка типов дисков

_Пример запроса:_

```http request
GET _nn2/openstack/blockStorage/volumeTypes
```

##### Возвращаемые данные

- `id` – ID типа диска

- `name` – название типа диска

- `specs` – параметры типа (ключ-значение)

---

## Подраздел `Snapshots`

### _nn2/openstack/blockStorage/snapshots

#### Получение списка снимков дисков

_Пример запроса:_

```http request
GET _nn2/openstack/blockStorage/snapshots
```

##### Возвращаемые данные

- `id` - id снимка

- `name` - название снимка

- `description` - описание снимка

- `volumeId` - id диска

- `status` - статус хранения снимка

- `size` - размер снимка (в Гб)

- `created` - дата создания

### _nn2/openstack/blockStorage/snapshots/create

#### Создание снимка диска

_Пример запроса:_

```http request
POST _nn2/openstack/blockStorage/snapshots/create

{
    "snapshotName": "Ubuntu Server Volume Snapshot"
    "volumeName": "Ubuntu Server Volume",
    "description": "Bootable Ubuntu Volume Snapshot"
}
```

---

## Подраздел `Backups`

### _nn2/openstack/blockStorage/backups

#### Получение списка резервных копий дисков

_Пример запроса:_

```http request
GET _nn2/openstack/blockStorage/backups
```

##### Возвращаемые данные

- `id` – ID резервной копии

- `name` – название резервной копии

- `description` – описание резервной копии

- `volumeId` – ID диска, к которому относится резервная копия

- `status` – статус резервной копии

- `size` – размер (в Гб)

- `createdAt` – дата создания

- `failReason` – причина ошибки (если есть)

---

# Раздел `Networking`

## Подраздел `Networks`

### _nn2/openstack/networking/networks

#### Получение списка сетей

_Пример запроса:_

```http request
GET _nn2/openstack/networking/networks
```

##### Возвращаемые данные

- `id` - id сети

- `name` - название сети

- `status` - статус работы сети

- `external` - является внешней сетью?

- `shared` - является общей сетью?

- `tenantId` - id проекта сети

- `mtu` - MTU сети

- `subnets` - id подсетей

#### _nn2/openstack/networking/networks/create

#### Создание сети

_Пример запроса:_

```http request
POST _nn2/openstack/networking/networks/create

{
    "networkName": "RENDERMAN_NETWORK",
    "projectName": "admin",
    "isShared": true,
    "isExternal": true,
    "isAdmin": true,
    "networkType": "VLAN",
    "physicalNetwork": "192.1.0.2",
    "segmentId": "56448132147"
}
```

---

## Подраздел `Subnets`

### _nn2/openstack/networking/subnets

#### Получение списка подсетей

_Пример запроса:_

```http request
GET _nn2/openstack/networking/subnets
```

##### Возвращаемые данные

- `id` - id подсети

- `name` - название подсети

- `ipVersion` - версия ip

- `dns` - dns сервера

- `pools` - пулы для выделения

- `routes` - маршруты узла

- `gateway` - ip шлюза

- `_cidr` - CIDR

- `ipV6AddressMode` - режим конфигурации

- `ipV6RaMode` - RaMode IPv6

### _nn2/openstack/networking/subnets/create

#### Создание подсети

_Пример запроса:_

```http request
POST _nn2/openstack/networking/subnets/create

{
    "networkName": "RENDERMAN_NETWORK",
    "subnetName": "DefaultSubnet",
    "ipVersion": "IPv4",
    "cidr": "10.1.0.0/22",
    "gateway": "172.29.248.1",
    "start": "10.1.0.1",
    "end": "10.1.3.254",
    "destination": null,
    "nextHop": null,
    "dnsServer": null,
    "raMode": null,
    "addressMode": null
}
```

---

## Подраздел `Routers`

### _nn2/openstack/networking/routers

#### Получение списка маршрутизаторов

_Пример запроса:_

```http request
GET _nn2/openstack/networking/routers
```

##### Возвращаемые данные

- `id` – ID маршрутизатора

- `name` – название маршрутизатора

- `status` – статус маршрутизатора

- `distributed` – статус "общего" маршрутизатора

- `isAdmin` – статус администрирования

- `routes` – список маршрутов

- `externalNetworkId` – ID внешней сети

- `snat` – использование SNAT

---

## Подраздел `Floating Ips`

### _nn2/openstack/networking/floatingIps

#### Получение списка плавающих IP

_Пример запроса:_

```http request
GET _nn2/openstack/networking/floatingIps
```

##### Возвращаемые данные

- `id` – ID плавающего IP

- `tenantId` – ID проекта

- `router` – маршрутизатор, связанный с IP

- `fixedIpAddress` – фиксированный IP-адрес

- `floatingNetworkId` – ID сети плавающего IP

---

## Подраздел `Security Groups`

### _nn2/openstack/networking/securityGroups

#### Получение списка групп безопасности

_Пример запроса:_

```http request
GET _nn2/openstack/networking/securityGroups
```

##### Возвращаемые данные

- `id` – ID группы безопасности

- `name` – название группы безопасности

- `description` – описание группы безопасности

- `rules` – ID правил группы безопасности

---

## Подраздел `Security Rules`

### _nn2/openstack/networking/securityRules

#### Получение списка правил групп безопасности

_Пример запроса:_

```http request
GET _nn2/openstack/networking/securityRules
```

##### Возвращаемые данные

- `id` – ID правила безопасности

- `securityGroup` – связанная группа безопасности

- `description` – описание правила

- `protocol` – протокол (tcp/udp/http и др.)

- `remoteIpPrefix` – удалённая IP-подсеть (CIDR)

- `direction` – направление (ingress/egress)

- `etherType` – тип Ethernet (IPv4/IPv6)

- `portRangeMin` – минимальный порт

- `portRangeMax` – максимальный порт

---

## Подраздел `Ports`

### _nn2/openstack/networking/ports

#### Получение списка портов

_Пример запроса:_

```http request
GET _nn2/openstack/networking/ports
```

##### Возвращаемые данные

- `id` – ID порта

- `name` – название порта

- `device` – устройство, которому принадлежит порт

- `network` – сеть, к которой привязан порт

- `isAdmin` – статус администрирования

- `isSecured` – статус защищённого порта

- `secGroups` – список групп безопасности, связанных с портом

---

# Раздел `Identity`

## Подраздел `Users`

### _nn2/openstack/identity/users

#### Получение списка пользователей

_Пример запроса:_

```http request
GET _nn2/openstack/identity/users
```

##### Возвращаемые данные

- `id` – ID пользователя

- `name` – имя пользователя

- `description` – описание пользователя

- `email` – email пользователя

- `domainId` – ID домена

- `enabled` – статус активности пользователя

- `defaultProjectId` – ID проекта по умолчанию

---

## Подраздел `Groups`

### _nn2/openstack/identity/groups

#### Получение списка групп

_Пример запроса:_

```http request
GET _nn2/openstack/identity/groups
```

##### Возвращаемые данные

- `id` – ID группы

- `name` – название группы

- `description` – описание группы

- `domainId` – ID домена

- `groupUsers` – список пользователей группы

---

## Подраздел `Projects`

### _nn2/openstack/identity/projects

#### Получение списка проектов

_Пример запроса:_

```http request
GET _nn2/openstack/identity/projects
```

##### Возвращаемые данные

- `id` – ID проекта

- `name` – название проекта

- `description` – описание проекта

- `domainId` – ID домена

- `enabled` – статус активности проекта

- `parentId` – ID родительского проекта

---

## Подраздел `Domains`

### _nn2/openstack/identity/domains

#### Получение списка доменов

_Пример запроса:_

```http request
GET _nn2/openstack/identity/domains
```

##### Возвращаемые данные

- `id` – ID домена

- `name` – название домена

- `description` – описание домена

- `enabled` – статус активности домена